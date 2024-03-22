package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.*;
import com.backoffice.operations.payloads.*;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.*;
import com.backoffice.operations.service.TransferLimitService;
import com.backoffice.operations.service.TransferService;
import com.backoffice.operations.utils.ApiCaller;
import com.backoffice.operations.utils.CommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class TransferServiceImpl implements TransferService {

    private static final Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);
    private final CommonUtils commonUtils;
    private final RestTemplate restTemplate;
    private final ApiCaller apiCaller;
    @Value("${external.api.accounts}")
    private String accountExternalAPI;
    @Autowired
    @Qualifier("jwtAuth")
    private RestTemplate jwtAuthRestTemplate;
    @Value("${bank.transfer.url}")
    private String bankTransferUrl;

    @Autowired
    private SourceOperationRepository sourceOperationRepository;
    @Autowired
    private TransferAccountFieldsRepository transferAccountFieldsRepository;
    @Autowired
    private TransactionPurposeRepository transactionPurposeRepository;
    @Autowired
    private AccountCurrencyRepository AccountCurrencyRepository;
    @Autowired
    private SequenceCounterRepository sequenceCounterRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    private final ObjectMapper objectMapper;

    private final TransferLimitService transferLimitService;

    public TransferServiceImpl(CommonUtils commonUtils, RestTemplate restTemplate, ApiCaller apiCaller, ObjectMapper objectMapper, TransferLimitService transferLimitService) {
        this.commonUtils = commonUtils;
        this.restTemplate = restTemplate;
        this.apiCaller = apiCaller;
        this.objectMapper = objectMapper;
        this.transferLimitService = transferLimitService;
    }

    @Override
    public GenericResponseDTO<Object> transferToBank(SelfTransferDTO selfTransferDTO) {
        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
        Map<String, Object> data = new HashMap<>();
        String transactionRefcode = "TRST";
        String refId = String.format("%012d", getNextSequence());
        String txnRefId = transactionRefcode + refId;
        String trnxDate = "";
        try {
            TransferRequestDto transferRequestDto = new TransferRequestDto();

            //set Sender details
            TransferRequestDto.Sender sender = transferRequestDto.new Sender();
            sender.setAccount_number(selfTransferDTO.getFromAccountNumber());
            String senderCifNo = selfTransferDTO.getFromAccountNumber().substring(3, 10);

            Optional<AccountDetails> senderAccountInfo = getTokenAndApiResponse(senderCifNo);
            senderAccountInfo.ifPresent(accountInfo -> {
                if (accountInfo.getResponse() != null && accountInfo.getResponse().getPayload() != null &&
                        accountInfo.getResponse().getPayload().getCustSummaryDetails() != null) {
                    List<AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount> islamicAccounts = accountInfo.getResponse().getPayload().getCustSummaryDetails().getIslamicAccounts();
                    for (AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount islamicAccount : islamicAccounts) {
                        if (islamicAccount.getAcc().equalsIgnoreCase(selfTransferDTO.getFromAccountNumber())) {
                            sender.setAccount_name(islamicAccount.getAdesc());
                            break;
                        }
                    }
                }
            });
//            String senderAccName = senderAccountInfo.get().getResponse().getPayload().getCustSummaryDetails().getIslamicAccounts().get(0).getAdesc();
//            sender.setAccount_name(senderAccName);
            AccountCurrency accountCurrency = AccountCurrencyRepository.findByAccountCurrencyCode("omr");
            sender.setAccount_currency(accountCurrency.getAccountCurrency());
            sender.setBank_code("IZZB");
            sender.setBank_name("Alizz Islamic Bank");
            sender.setBranch_code(selfTransferDTO.getFromAccountNumber().substring(0, 3));

            //set Receiver details
            TransferRequestDto.Receiver receiver = transferRequestDto.new Receiver();
            receiver.setAccount_number(selfTransferDTO.getToAccountNumber());
            String receiverCifNo = selfTransferDTO.getToAccountNumber().substring(3, 10);
            // below code commented for testing
            Optional<AccountDetails> receiverAccountInfo = getTokenAndApiResponse(receiverCifNo);
            String receiverCifNoAccName = receiverAccountInfo.get().getResponse().getPayload().getCustSummaryDetails().getIslamicAccounts().get(0).getAdesc();
            receiver.setAccount_name(receiverCifNoAccName);
            receiver.setBank_code("IZZB");
            receiver.setBank_name("Alizz Islamic Bank");
            receiver.setBranch_code(selfTransferDTO.getFromAccountNumber().substring(0, 3));
            receiver.setIban_account_number("");
            receiver.setBank_address1("Muscat");
            receiver.setBank_address2("Muscat");
            receiver.setBank_address3("Muscat");
            receiver.setBank_address4("Muscat");
            receiver.setBene_address1("Muscat");
            receiver.setBene_address2("Muscat");
            receiver.setBene_address3("Muscat");
            receiver.setBene_address4("Muscat");
            receiver.setNotes_to_receiver(selfTransferDTO.getNotesToReceiver());
            receiver.setBank_country("Oman");

            //set header
            TransferRequestDto.Header header = transferRequestDto.new Header();
            header.setSource_system("mpp-digital-app");
            header.setSource_user(selfTransferDTO.getUniqueKey());
            SourceOperation SourceOperation = sourceOperationRepository.findBySourceCode("SelfTransfer");
            header.setSource_operation(SourceOperation.getSourceOperation());

            trnxDate = commonUtils.getBankDate(selfTransferDTO.getFromAccountNumber().substring(0, 3));

            //set transaction
            TransferRequestDto.Transaction transaction = transferRequestDto.new Transaction();
            TransferAccountFields TransferAccountFields = transferAccountFieldsRepository.findByTransferType("SELF TRANSFER");
            transaction.setTransaction_reference(txnRefId);
            transaction.setTransaction_date(trnxDate);
            TransactionPurpose transactionPurpose = transactionPurposeRepository.findByTransactionPurposeCode("ordinaryTransfer");
            transaction.setTransaction_purpose(transactionPurpose.getTransactionPurpose());
            transaction.setTransaction_currency(accountCurrency.getAccountCurrency());
            transaction.setCbs_product(TransferAccountFields.getCbsProduct());
            transaction.setCbs_module(TransferAccountFields.getCbsModule());
            transaction.setCbs_network(TransferAccountFields.getCbsNetwork());
            transaction.setCharge_type(TransferAccountFields.getChargeType());
            transaction.setPayment_details_1(selfTransferDTO.getNotesToReceiver());
            transaction.setPayment_details_2("");
            transaction.setPayment_details_3("");
            transaction.setPayment_details_4("");

            transaction.setTransaction_amount(selfTransferDTO.getTransactionAmount());

            double avlBalance = apiCaller.getAvailableBalance(selfTransferDTO.getFromAccountNumber());
            if (avlBalance > selfTransferDTO.getTransactionAmount()) {
                transaction.setTransaction_amount(selfTransferDTO.getTransactionAmount());
            } else {
                data.put("message", "Payment failed!");
                data.put("transactionID", txnRefId);
                data.put("transactionDateTime", trnxDate);
                data.put("uniqueKey", selfTransferDTO.getUniqueKey());
                data.put("status", "Failure");
                responseDTO.setStatus("Success");
                responseDTO.setMessage("Failure");
                responseDTO.setData(data);
            }
            transferRequestDto.setSender(sender);
            transferRequestDto.setReceiver(receiver);
            transferRequestDto.setHeader(header);
            transferRequestDto.setTransaction(transaction);


            GenericResponseDTO<Object> responseObject = transferLimitService.getTransferLimit(selfTransferDTO.getCustomerType(),
                    selfTransferDTO.getUniqueKey(), selfTransferDTO.getTransactionType(), selfTransferDTO.getTransactionAmount());
            Object map = responseObject.getData();
            Map<String, Object> resMap = objectMapper.convertValue(map, new TypeReference<Map<String, Object>>() {
            });
            if (resMap.containsKey("isTrxnAllowed") && resMap.get("isTrxnAllowed").equals(true)) {
            ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(response.getBody().getAccessToken());
            ObjectMapper objectMapper = JsonMapper.builder()
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .build();
            String jsonRequestBody = objectMapper.writeValueAsString(transferRequestDto);
            logger.info("jsonRequestBody: {}", jsonRequestBody);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequestBody, headers);
            ResponseEntity<FundTransferResponseDto> responseEntity = restTemplate.exchange(bankTransferUrl, HttpMethod.POST, requestEntity, FundTransferResponseDto.class);

            FundTransferResponseDto fundTransferResponseDto = responseEntity.getBody();
            logger.info("responseEntity.getBody(): {}", responseEntity.getBody());

            responseDTO = getResponseDto(selfTransferDTO.getUniqueKey(), responseEntity.getStatusCode().is2xxSuccessful(),
                    fundTransferResponseDto, txnRefId, trnxDate, selfTransferDTO.getTransactionAmount(), selfTransferDTO.getFromAccountNumber());
        }
        } catch (Exception e) {
            logger.error("ERROR in class TransferServiceImpl method transferToBank", e);
            data.put("message", "Payment failed!");
            data.put("transactionID", txnRefId);
            data.put("transactionDateTime", trnxDate);
            data.put("uniqueKey", selfTransferDTO.getUniqueKey());
            data.put("status", "Failure");
            responseDTO.setStatus("Success");
            responseDTO.setMessage("Failure");
            responseDTO.setData(data);
            return responseDTO;
        }
        return responseDTO;
    }

    public GenericResponseDTO<Object> getResponseDto(String uniqueKey, boolean isSuccessful, FundTransferResponseDto fundTransferResponseDto, String txnRefId, String txnDate, Double transactionAmount, String accountNumber) throws JsonProcessingException {

        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
        Map<String, Object> data = new HashMap<>();

        if (isSuccessful && Objects.nonNull(fundTransferResponseDto) && fundTransferResponseDto.isSuccess()) {

            List<FundTransferResponseDto.Fcubswarningresp> warningResponse = Objects.nonNull(fundTransferResponseDto.getResponse().getResult()
                    .getFcubswarningresp()) ? fundTransferResponseDto.getResponse().getResult().getFcubswarningresp() : new ArrayList<>();

            List<FundTransferResponseDto.Fcubserrorresp> errorResponse = Objects.nonNull(fundTransferResponseDto.getResponse().getResult()
                    .getFcubserrorresp()) ? fundTransferResponseDto.getResponse().getResult().getFcubserrorresp() : new ArrayList<>();

            Long responseTxnRefNo = fundTransferResponseDto.getResponse().getResult().getCstmrCdtTrfInitn().getGrpTlr().getTxnRefNo();
            String reqExctnDt = fundTransferResponseDto.getResponse().getResult().getCstmrCdtTrfInitn().getPmtInf().getReqdExctnDt();

            if (errorResponse.isEmpty()) {
                String warningResponseString = objectMapper.writeValueAsString(!warningResponse.isEmpty() ? warningResponse : "");
                Transaction transactionObj = Transaction.builder()
                        .responseTxnReferenceId(String.valueOf(responseTxnRefNo)).txnReferenceId(txnRefId)
                        .txnStatus("Success")
                        .warningResponse(warningResponseString)
                        .txnDate(reqExctnDt).uniqueKey(uniqueKey).build();

                transactionRepository.save(transactionObj);
                data.put("transactionID", txnRefId);
                data.put("uniqueKey", uniqueKey);
                data.put("transactionDateTime", reqExctnDt);
                data.put("message", "Payment successful!");
                data.put("status", "Success");
                responseDTO.setData(data);
                responseDTO.setMessage("Success");
                responseDTO.setStatus("Success");

                transferLimitService.saveUserTrxnLimitData(uniqueKey, transactionAmount
                        , accountNumber);
            } else {
                String errorResponseString = objectMapper.writeValueAsString(!errorResponse.isEmpty() ? errorResponse : "");
                Transaction transactionObj = Transaction.builder()
                        .responseTxnReferenceId(String.valueOf(responseTxnRefNo)).txnReferenceId(txnRefId)
                        .txnStatus("Failure")
                        .errorResponse(errorResponseString)
                        .txnDate(reqExctnDt).uniqueKey(uniqueKey).build();

                transactionRepository.save(transactionObj);
                data.put("transactionID", txnRefId);
                data.put("uniqueKey", uniqueKey);
                data.put("transactionDateTime", reqExctnDt);
                data.put("message", "Payment failed!");
                data.put("status", "Failure");
                data.put("error", errorResponse);
                responseDTO.setData(data);
                responseDTO.setMessage("Failure");
                responseDTO.setStatus("Success");
            }
        } else if (isSuccessful && Objects.nonNull(fundTransferResponseDto) && !fundTransferResponseDto.isSuccess()) {
            Long responseTxnRefNo = fundTransferResponseDto.getResponse().getResult().getCstmrCdtTrfInitn().getGrpTlr().getTxnRefNo();
            String reqExctnDt = fundTransferResponseDto.getResponse().getResult().getCstmrCdtTrfInitn().getPmtInf().getReqdExctnDt();

            List<FundTransferResponseDto.Fcubswarningresp> warningResponse = Objects.nonNull(fundTransferResponseDto.getResponse().getResult()
                    .getFcubswarningresp()) ? fundTransferResponseDto.getResponse().getResult().getFcubswarningresp() : new ArrayList<>();

            List<FundTransferResponseDto.Fcubserrorresp> errorResponse = Objects.nonNull(fundTransferResponseDto.getResponse().getResult()
                    .getFcubserrorresp()) ? fundTransferResponseDto.getResponse().getResult().getFcubserrorresp() : new ArrayList<>();

            String warningResponseString = objectMapper.writeValueAsString(!warningResponse.isEmpty() ? warningResponse : "");
            String errorResponseString = objectMapper.writeValueAsString(!errorResponse.isEmpty() ? errorResponse : "");
            Transaction transactionObj = Transaction.builder()
                    .responseTxnReferenceId(String.valueOf(responseTxnRefNo)).txnReferenceId(txnRefId)
                    .txnStatus("Failure")
                    .warningResponse(warningResponseString)
                    .errorResponse(errorResponseString)
                    .txnDate(reqExctnDt).uniqueKey(uniqueKey).build();

            transactionRepository.save(transactionObj);
            data.put("transactionID", txnRefId);
            data.put("uniqueKey", uniqueKey);
            data.put("transactionDateTime", reqExctnDt);
            data.put("message", "Payment failed!");
            data.put("status", "Failure");
            data.put("error", errorResponse);
            responseDTO.setData(data);
            responseDTO.setMessage("Failure");
            responseDTO.setStatus("Success");
        }
        return responseDTO;
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