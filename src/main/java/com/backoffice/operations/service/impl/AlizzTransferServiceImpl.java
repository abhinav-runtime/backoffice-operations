package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.*;
import com.backoffice.operations.payloads.*;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.*;
import com.backoffice.operations.service.AlizzTransferService;
import com.backoffice.operations.service.BeneficiaryService;
import com.backoffice.operations.utils.CommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlizzTransferServiceImpl implements AlizzTransferService {


    @Value("${external.api.transfer.bank}")
    private String alizzBankTransfer;

    private final CommonUtils commonUtils;

    private final RestTemplate restTemplate;

    private final BeneficiaryService beneficiaryService;

    @Value("${external.api.accounts}")
    private String accountExternalAPI;

    @Autowired
    @Qualifier("jwtAuth")
    private RestTemplate jwtAuthRestTemplate;

    private final TransferAccountFieldsRepository transferAccountFieldsRepository;

    private final SourceOperationRepository sourceOperationRepository;

    private final AccountCurrencyRepository accountCurrencyRepository;

    private final SequenceCounterRepository sequenceCounterRepository;
    private final TransactionRepository transactionRepository;

    private final BeneficiaryBankRepository beneficiaryBankRepository;

    public AlizzTransferServiceImpl(CommonUtils commonUtils, RestTemplate restTemplate, BeneficiaryService beneficiaryService, TransferAccountFieldsRepository transferAccountFieldsRepository, SourceOperationRepository sourceOperationRepository, AccountCurrencyRepository accountCurrencyRepository, SequenceCounterRepository sequenceCounterRepository, TransactionRepository transactionRepository, BeneficiaryBankRepository beneficiaryBankRepository) {
        this.commonUtils = commonUtils;
        this.restTemplate = restTemplate;
        this.beneficiaryService = beneficiaryService;
        this.transferAccountFieldsRepository = transferAccountFieldsRepository;
        this.sourceOperationRepository = sourceOperationRepository;
        this.accountCurrencyRepository = accountCurrencyRepository;
        this.sequenceCounterRepository = sequenceCounterRepository;
        this.transactionRepository = transactionRepository;
        this.beneficiaryBankRepository = beneficiaryBankRepository;
    }

    @Override
    public GenericResponseDTO<Object> transferToAlizzAccount(AlizzTransferRequestDto alizzTransferRequestDto) throws JsonProcessingException {

        ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
        if (Objects.nonNull(response.getBody())) {

            AlizzTransferResponseDto alizzTransferResponseDto = new AlizzTransferResponseDto();

            String transactionRefcode = "TRWA";
            String refId = String.format("%012d", getNextSequence());
            String txnRefId = transactionRefcode + refId;
            String txnDate = LocalDateTime.now().toString();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(response.getBody().getAccessToken());

            TransferAccountFields transferAccountFields = transferAccountFieldsRepository.findByTransferType("WITHIN ALIZZ");

            SourceOperation sourceOperation = sourceOperationRepository.findAll().get(0);

            AccountCurrency accountCurrency = accountCurrencyRepository.findAll().get(0);
            BeneficiaryBank beneficiaryBank = beneficiaryBankRepository.findByBankName("Alizz bank");

            AlizzTransferDto alizzTransferDto = new AlizzTransferDto();
            AlizzTransferDto.Header header = new AlizzTransferDto.Header();
            header.setSource_system(sourceOperation.getSourceSystem());
            header.setSource_user(alizzTransferRequestDto.getUniqueKey());
            header.setSource_operation(sourceOperation.getSourceOperation());

            AlizzTransferDto.Transaction transaction = getTransactionDetails(alizzTransferRequestDto, txnRefId, txnDate, accountCurrency, transferAccountFields);


            AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount senderAccDetails = getIslamicAccount(alizzTransferRequestDto.getFromAccountNumber());

            if (Objects.isNull(senderAccDetails)) {
                return getErrorResponseGenericDTO(alizzTransferRequestDto, "Sender Account Invalid");
            }

            AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount receiverAccDetails = getIslamicAccount(alizzTransferRequestDto.getToAccountNumber());

            if (Objects.isNull(receiverAccDetails)) {
                return getErrorResponseGenericDTO(alizzTransferRequestDto, "Receiver Account Invalid");
            }

            AlizzTransferDto.Sender sender = getSenderDetails(alizzTransferRequestDto, senderAccDetails, beneficiaryBank);

            AlizzTransferDto.Receiver receiver = getReceiverDetails(alizzTransferRequestDto, receiverAccDetails);

            alizzTransferDto.setSender(sender);
            alizzTransferDto.setReceiver(receiver);
            alizzTransferDto.setHeader(header);
            alizzTransferDto.setTransaction(transaction);

            ObjectMapper objectMapper = JsonMapper.builder()
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .build();
            String jsonRequestBody = objectMapper.writeValueAsString(alizzTransferDto);

            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequestBody, headers);
            ResponseEntity<FundTransferResponseDto> responseEntity = restTemplate.exchange(alizzBankTransfer, HttpMethod.POST, requestEntity, FundTransferResponseDto.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Beneficiary beneficiary = beneficiaryService.addBeneficiary(alizzTransferDto.getReceiver());
                alizzTransferResponseDto.setAccountName(beneficiary.getAccountName());
                alizzTransferResponseDto.setAccountNumber(beneficiary.getAccountNumber());
                alizzTransferResponseDto.setBankName("Alizz bank");
                FundTransferResponseDto fundTransferResponseDto = responseEntity.getBody();

                if (Objects.nonNull(fundTransferResponseDto)) {
                    Integer responseTxnRefNo = fundTransferResponseDto.getResponseObject().getResultObject().getCstmrCdtTrfInitnObject().getGrpTlrObject().getTxnRefNo();
                    String errorResponse = objectMapper.writeValueAsString(Objects.nonNull(fundTransferResponseDto.getResponseObject().getResultObject().getFcubserrorresp()) ? fundTransferResponseDto.getResponseObject().getResultObject().getFcubserrorresp() : "");
                    Transaction transactionObj = Transaction.builder()
                            .responseTxnReferenceId(String.valueOf(responseTxnRefNo)).txnReferenceId(txnRefId)
                            .txnStatus(fundTransferResponseDto.isSuccess() ? "Success" : "Pending")
                            .errorResponse(errorResponse)
                            .txnDate(txnDate).uniqueKey(alizzTransferRequestDto.getUniqueKey()).build();

                    transactionRepository.save(transactionObj);
                    alizzTransferResponseDto.setTransactionNumber(String.valueOf(responseTxnRefNo));
                    alizzTransferResponseDto.setTransactionDate(txnDate);
                }
            }
            GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
            Map<String, Object> data = new HashMap<>();
            data.put("alizzTransferResponseDto", responseEntity.getBody());
            responseDTO.setStatus("Success");
            responseDTO.setMessage("Success");
            responseDTO.setData(data);
            return responseDTO;
        }
        return null;
    }

    private static AlizzTransferDto.Receiver getReceiverDetails(AlizzTransferRequestDto alizzTransferRequestDto, AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount receiverAccDetails) {
        AlizzTransferDto.Receiver receiver = AlizzTransferDto.Receiver.builder()
                .notesToReceiver(alizzTransferRequestDto.getNotesToReceiver())
                .accountName(receiverAccDetails.getAdesc()).accountNumber(receiverAccDetails.getAcc()).build();
        return receiver;
    }

    private static AlizzTransferDto.Sender getSenderDetails(AlizzTransferRequestDto alizzTransferRequestDto, AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount senderAccDetails, BeneficiaryBank beneficiaryBank) {
        AlizzTransferDto.Sender sender = AlizzTransferDto.Sender.builder().accountNumber(senderAccDetails.getAcc())
                .accountCurrency(senderAccDetails.getCcy()).accountName(senderAccDetails.getAdesc())
                .branchCode(Objects.nonNull(beneficiaryBank) ? beneficiaryBank.getBankCode() : "")
                .bankCode(alizzTransferRequestDto.getFromAccountNumber().substring(0, 3)).build();
        return sender;
    }

    private static AlizzTransferDto.Transaction getTransactionDetails(AlizzTransferRequestDto alizzTransferRequestDto, String txnRefId, String txnDate, AccountCurrency accountCurrency, TransferAccountFields transferAccountFields) {
        AlizzTransferDto.Transaction transaction = AlizzTransferDto.Transaction.builder()
                .paymentDetails1(alizzTransferRequestDto.getNotesToReceiver()).paymentDetails2("")
                .transactionReference(txnRefId).transactionDate(txnDate)
                .transactionAmount(alizzTransferRequestDto.getTransactionAmount())
                .transactionPurpose(alizzTransferRequestDto.getTransactionPurpose()).transactionCurrency(accountCurrency.getAccountCurrency())
                .cbsModule(Objects.nonNull(transferAccountFields) && Objects.nonNull(transferAccountFields.getCbsModule()) ? transferAccountFields.getCbsModule() : "")
                .cbsNetwork(Objects.nonNull(transferAccountFields) && Objects.nonNull(transferAccountFields.getCbsNetwork()) ? transferAccountFields.getCbsNetwork() : "")
                .cbsProduct(Objects.nonNull(transferAccountFields) && Objects.nonNull(transferAccountFields.getCbsProduct()) ? transferAccountFields.getCbsProduct() : "")
                .chargeType(Objects.nonNull(transferAccountFields) && Objects.nonNull(transferAccountFields.getChargeType()) ? transferAccountFields.getChargeType() : "")
                .build();
        return transaction;
    }

    private static GenericResponseDTO<Object> getErrorResponseGenericDTO(AlizzTransferRequestDto alizzTransferRequestDto, String Receiver_Account_Invalid) {
        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
        Map<String, Object> data = new HashMap<>();
        data.put("uniqueKey", alizzTransferRequestDto.getUniqueKey());
        data.put("message", Receiver_Account_Invalid);
        responseDTO.setStatus("Failure");
        responseDTO.setMessage("Failure");
        responseDTO.setData(data);
        return responseDTO;
    }

    private AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount getIslamicAccount(String accountNumber) {
        String receiverCifNo = accountNumber.substring(3, 10);
        Optional<AccountDetails> receiverAccountInfo = getTokenAndApiResponse(receiverCifNo);
        if (receiverAccountInfo.isPresent()) {
            List<AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount> accountDetails = receiverAccountInfo.get().getResponse().getPayload().getCustSummaryDetails().getIslamicAccounts();
            return accountDetails.stream()
                    .filter(acc -> acc.getAcc().equals(accountNumber))
                    .toList().get(0);
        } else {
            return null;
        }
    }

    private Optional<AccountDetails> getTokenAndApiResponse(String civilId) {
        ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
        if (Objects.nonNull(response.getBody())) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(response.getBody().getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String apiUrl = accountExternalAPI + civilId;
            ResponseEntity<AccountDetails> responseEntity = jwtAuthRestTemplate.exchange(apiUrl, HttpMethod.GET, entity, AccountDetails.class);
            return Optional.ofNullable(responseEntity.getBody());
        }
        return Optional.empty();
    }

    private synchronized long getNextSequence() {
        SequenceCounter sequenceCounter = sequenceCounterRepository.findById(1L).orElseGet(() -> {
            SequenceCounter newCounter = new SequenceCounter();
            newCounter.setNextValue(0L);
            return sequenceCounterRepository.save(newCounter);
        });

        long nextValue = sequenceCounter.getNextValue() + 1;
        sequenceCounter.setNextValue(nextValue);

        sequenceCounterRepository.save(sequenceCounter);

        return nextValue;
    }
}
