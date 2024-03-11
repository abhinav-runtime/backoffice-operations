package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.*;
import com.backoffice.operations.payloads.*;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.*;
import com.backoffice.operations.service.TransferService;
import com.backoffice.operations.utils.ApiCaller;
import com.backoffice.operations.utils.CommonUtils;
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

import java.time.LocalDateTime;
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

    public TransferServiceImpl(CommonUtils commonUtils, RestTemplate restTemplate, ApiCaller apiCaller) {
        this.commonUtils = commonUtils;
        this.restTemplate = restTemplate;
        this.apiCaller = apiCaller;
    }

    @Override
    public GenericResponseDTO<Object> transferToBank(SelfTransferDTO selfTransferDTO) {
        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
        try {
            TransferRequestDto transferRequestDto = new TransferRequestDto();

            String transactionRefcode = "TRST";
            String refId = String.format("%012d", getNextSequence());
            String txnRefId = transactionRefcode + refId;
            String txnDate = LocalDateTime.now().toString();

            //set Sender details
            TransferRequestDto.Sender sender = transferRequestDto.new Sender();
            sender.setAccount_number(selfTransferDTO.getFromAccountNumber());
            String senderCifNo = selfTransferDTO.getFromAccountNumber().substring(3, 10);

            // below code commented for testing
            Optional<AccountDetails> senderAccountInfo = getTokenAndApiResponse(senderCifNo);
            String senderAccName = senderAccountInfo.get().getResponse().getPayload().getCustSummaryDetails().getIslamicAccounts().get(0).getAdesc();
            sender.setAccount_name(senderAccName);
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

            //set transaction
            TransferRequestDto.Transaction transaction = transferRequestDto.new Transaction();
            TransferAccountFields TransferAccountFields = transferAccountFieldsRepository.findByTransferType("SELF TRANSFER");
            transaction.setTransaction_reference(txnRefId);
            transaction.setTransaction_date(commonUtils.getBankDate(selfTransferDTO.getFromAccountNumber().substring(0, 3)));
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
                throw new RuntimeException("Insufficient balance for the transaction");
            }
            transferRequestDto.setSender(sender);
            transferRequestDto.setReceiver(receiver);
            transferRequestDto.setHeader(header);
            transferRequestDto.setTransaction(transaction);

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

            Map<String, Object> data = new HashMap<>();

            FundTransferResponseDto fundTransferResponseDto = responseEntity.getBody();
            logger.info("responseEntity.getBody(): {}", responseEntity.getBody());

            if (responseEntity.getStatusCode().is2xxSuccessful() && Objects.nonNull(fundTransferResponseDto) && fundTransferResponseDto.isSuccess()
                    && fundTransferResponseDto.getMessage().equals("Fund Transfer is successful at Core Banking")) {

                List<FundTransferResponseDto.Fcubswarningresp> warningResponse =  Objects.nonNull(fundTransferResponseDto.getResponse().getResult()
                        .getFcubswarningresp()) ? fundTransferResponseDto.getResponse().getResult().getFcubswarningresp() : new ArrayList<>();

                boolean isFutureDateError = false;
                if(!warningResponse.isEmpty()){
                   List<FundTransferResponseDto.Warning> warningObject = warningResponse.stream()
                           .flatMap(fcubswarningresp -> fcubswarningresp.getWarning().stream())
                           .filter(warning -> warning.getWdesc().equals("Value Date is in Future. Payment is Rescheduled."))
                           .toList();
                    if (!warningObject.isEmpty()) {
                        isFutureDateError = true;
                    }
                }

                Long responseTxnRefNo = fundTransferResponseDto.getResponse().getResult().getCstmrCdtTrfInitn().getGrpTlr().getTxnRefNo();
                String reqExctnDt = fundTransferResponseDto.getResponse().getResult().getCstmrCdtTrfInitn().getPmtInf().getReqdExctnDt();
                String warningResponseString =  objectMapper.writeValueAsString(warningResponse);
                Transaction transactionObj = Transaction.builder()
                        .responseTxnReferenceId(String.valueOf(responseTxnRefNo)).txnReferenceId(txnRefId)
                        .txnStatus("Success")
                        .warningResponse(warningResponseString)
                        .txnDate(reqExctnDt).uniqueKey(selfTransferDTO.getUniqueKey()).build();

                if (isFutureDateError){
                    data.put("message", "Payment successful and it will be settled on next working days!");
                }else {
                    data.put("message", "Payment successful!");
                }

                transactionRepository.save(transactionObj);
                data.put("transactionID", responseTxnRefNo);
                data.put("transactionDateTime",reqExctnDt);
                data.put("warning", warningResponse);
            } else if (responseEntity.getStatusCode().is2xxSuccessful() && Objects.nonNull(fundTransferResponseDto) && fundTransferResponseDto.isSuccess()
                    && fundTransferResponseDto.getMessage().isEmpty()) {
                Long responseTxnRefNo = fundTransferResponseDto.getResponse().getResult().getCstmrCdtTrfInitn().getGrpTlr().getTxnRefNo();
                String reqExctnDt = fundTransferResponseDto.getResponse().getResult().getCstmrCdtTrfInitn().getPmtInf().getReqdExctnDt();

                List<FundTransferResponseDto.Fcubserrorresp> errorResponse =  Objects.nonNull(fundTransferResponseDto.getResponse().getResult()
                        .getFcubserrorresp()) ? fundTransferResponseDto.getResponse().getResult().getFcubserrorresp() : new ArrayList<>();

                String errorResponseString = objectMapper.writeValueAsString(errorResponse);
                Transaction transactionObj = Transaction.builder()
                        .responseTxnReferenceId(String.valueOf(responseTxnRefNo)).txnReferenceId(txnRefId)
                        .txnStatus("Failure")
                        .errorResponse(errorResponseString)
                        .txnDate(reqExctnDt).uniqueKey(selfTransferDTO.getUniqueKey()).build();

                transactionRepository.save(transactionObj);
                List<FundTransferResponseDto.Fcubswarningresp> warning = fundTransferResponseDto.getResponse().getResult().getFcubswarningresp();
                data.put("message", "Invalid account details!");
                data.put("transactionID", responseTxnRefNo);
                data.put("transactionDateTime",reqExctnDt);
                data.put("error", errorResponse);
            } else if (responseEntity.getStatusCode().is2xxSuccessful() && Objects.nonNull(fundTransferResponseDto) && !fundTransferResponseDto.isSuccess()
                    && fundTransferResponseDto.getMessage().equals("Fund Transfer is not successful at Core Banking")) {
                Long responseTxnRefNo = fundTransferResponseDto.getResponse().getResult().getCstmrCdtTrfInitn().getGrpTlr().getTxnRefNo();
                String reqExctnDt = fundTransferResponseDto.getResponse().getResult().getCstmrCdtTrfInitn().getPmtInf().getReqdExctnDt();
                FundTransferResponseDto.Fcubswarningresp warningResponse =  Objects.nonNull(fundTransferResponseDto.getResponse().getResult()
                        .getFcubswarningresp()) ? fundTransferResponseDto.getResponse().getResult().getFcubswarningresp().get(0) : new FundTransferResponseDto.Fcubswarningresp();

                String warningResponseString = objectMapper.writeValueAsString(warningResponse);
                Transaction transactionObj = Transaction.builder()
                        .responseTxnReferenceId(String.valueOf(responseTxnRefNo)).txnReferenceId(txnRefId)
                        .txnStatus("Failure")
                        .warningResponse(warningResponseString)
                        .txnDate(reqExctnDt).uniqueKey(selfTransferDTO.getUniqueKey()).build();

                transactionRepository.save(transactionObj);
                data.put("message", "Payment failed!");
                data.put("transactionID", responseTxnRefNo);
                data.put("transactionDateTime",reqExctnDt);
                data.put("warning", warningResponseString);

            }
            data.put("uniqueKey", selfTransferDTO.getUniqueKey());
            responseDTO.setStatus("Success");
            responseDTO.setMessage("Success");
            responseDTO.setData(responseEntity);
        } catch (Exception e) {
            logger.error("ERROR in class TransferServiceImpl method transferToBank", e);
            responseDTO.setMessage("Something went wrong");
            responseDTO.setStatus("Failure");
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