package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.*;
import com.backoffice.operations.payloads.*;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.*;
import com.backoffice.operations.service.TransferService;
import com.backoffice.operations.utils.ApiCaller;
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
import java.util.Objects;
import java.util.Optional;

@Service
public class TransferServiceImpl implements TransferService {

    private final CommonUtils commonUtils;

    @Value("${external.api.accounts}")
    private String accountExternalAPI;

    @Autowired
    @Qualifier("jwtAuth")
    private RestTemplate jwtAuthRestTemplate;

    private final RestTemplate restTemplate;
    public TransferServiceImpl(CommonUtils commonUtils, RestTemplate restTemplate, ApiCaller apiCaller) {
        this.commonUtils = commonUtils;
        this.restTemplate = restTemplate;
        this.apiCaller = apiCaller;
    }

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

    private final ApiCaller apiCaller;
    @Autowired
    private  SequenceCounterRepository sequenceCounterRepository;

    @Autowired
    private  TransactionRepository transactionRepository;


    @Override
    public GenericResponseDTO<Object> transferToBank(SelfTransferDTO selfTransferDTO) throws JsonProcessingException {
        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
        try{

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
            String senderAccName = senderAccountInfo.get().getResponse().getPayload().getCustSummaryDetails().getIslamicAccounts().get(0).getAccdesc();
            sender.setAccount_name(senderAccName);
            AccountCurrency accountCurrency = AccountCurrencyRepository.findByAccountCurrencyCode("omr");
            sender.setAccount_currency(accountCurrency.getAccountCurrency());

            //set Receiver details
            TransferRequestDto.Receiver receiver = transferRequestDto.new Receiver();
            receiver.setAccount_number(selfTransferDTO.getToAccountNumber());
            String receiverCifNo = selfTransferDTO.getToAccountNumber().substring(3, 10);
            // below code commented for testing
            Optional<AccountDetails> receiverAccountInfo = getTokenAndApiResponse(receiverCifNo);
            String receiverCifNoAccName = receiverAccountInfo.get().getResponse().getPayload().getCustSummaryDetails().getIslamicAccounts().get(0).getAccdesc();
            receiver.setAccount_name(receiverCifNoAccName);

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
            transaction.setTransaction_date(LocalDate.now().toString());
            TransactionPurpose transactionPurpose = transactionPurposeRepository.findByTransactionPurposeCode("ordinaryTransfer");
            transaction.setTransaction_purpose(transactionPurpose.getTransactionPurpose());
            transaction.setTransaction_currency(accountCurrency.getAccountCurrency());
            transaction.setCbs_product(TransferAccountFields.getCbsProduct());
            transaction.setCbs_module(TransferAccountFields.getCbsModule());
            transaction.setCbs_network(TransferAccountFields.getCbsNetwork());
            transaction.setCharge_type(TransferAccountFields.getChargeType());
            transaction.setPayment_details_1(selfTransferDTO.getNotesToReceiver());

            transaction.setTransaction_amount(selfTransferDTO.getTransactionAmount());

            Double avlBalance = apiCaller.getAvailableBalance(selfTransferDTO.getFromAccountNumber());
            if(avlBalance > selfTransferDTO.getTransactionAmount()){
                transaction.setTransaction_amount(selfTransferDTO.getTransactionAmount());
            }else{
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
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequestBody, headers);
            ResponseEntity<FundTransferResponseDto> responseEntity = restTemplate.exchange(bankTransferUrl, HttpMethod.POST, requestEntity, FundTransferResponseDto.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                FundTransferResponseDto fundTransferResponseDto = responseEntity.getBody();
                Integer responseTxnRefNo = fundTransferResponseDto.getResponseObject().getResultObject().getCstmrCdtTrfInitnObject().getGrpTlrObject().getTxnRefNo();
                String errorResponse = objectMapper.writeValueAsString(Objects.nonNull(fundTransferResponseDto.getResponseObject().getResultObject().getFcubserrorresp()) ? fundTransferResponseDto.getResponseObject().getResultObject().getFcubserrorresp() : "");
                Transaction transactionObj = Transaction.builder()
                        .responseTxnReferenceId(String.valueOf(responseTxnRefNo)).txnReferenceId(txnRefId)
                        .txnStatus(fundTransferResponseDto.isSuccess() ? "Success" : "Pending")
                        .errorResponse(errorResponse)
                        .txnDate(txnDate).uniqueKey(selfTransferDTO.getUniqueKey()).build();

                transactionRepository.save(transactionObj);
                //alizzTransferResponseDto.setTransactionNumber(String.valueOf(responseTxnRefNo));
                //alizzTransferResponseDto.setTransactionDate(txnDate);

            }
            responseDTO.setStatus("Success");
            responseDTO.setMessage("Success");
            responseDTO.setData(responseEntity);

        }catch (Exception e) {
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