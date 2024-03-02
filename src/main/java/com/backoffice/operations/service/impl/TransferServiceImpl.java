package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.AccountCurrency;
import com.backoffice.operations.entity.SourceOperation;
import com.backoffice.operations.entity.TransactionPurpose;
import com.backoffice.operations.entity.TransferAccountFields;
import com.backoffice.operations.payloads.AccessTokenResponse;
import com.backoffice.operations.payloads.AccountDetails;
import com.backoffice.operations.payloads.SelfTransferDTO;
import com.backoffice.operations.payloads.TransferRequestDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.AccountCurrencyRepository;
import com.backoffice.operations.repository.SourceOperationRepository;
import com.backoffice.operations.repository.TransactionPurposeRepository;
import com.backoffice.operations.repository.TransferAccountFieldsRepository;
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


    @Override
    public GenericResponseDTO<Object> transferToBank(SelfTransferDTO selfTransferDTO) throws JsonProcessingException {

        TransferRequestDto transferRequestDto = new TransferRequestDto();

        String transactionRefcode = "TRWA";
        //String refId = String.format("%012d", getNextSequence());
        //String txnRefId = transactionRefcode + refId;

        //set Sender details
        TransferRequestDto.Sender sender = transferRequestDto.new Sender();
        sender.setAccount_number(selfTransferDTO.getFromAccountNumber());
        String senderCifNo = selfTransferDTO.getFromAccountNumber().substring(3, 10);

        // below code commented for testing
        //Optional<AccountDetails> senderAccountInfo = getTokenAndApiResponse(senderCifNo);
        //String senderAccName = senderAccountInfo.get().getResponse().getPayload().getCustSummaryDetails().getIslamicAccounts().get(0).getAccdesc();
        //sender.setAccount_name(senderAccName);
        AccountCurrency accountCurrency = AccountCurrencyRepository.findByAccountCurrencyCode("omr");
        sender.setAccount_currency(accountCurrency.getAccountCurrency());

        //set Receiver details
        TransferRequestDto.Receiver receiver = transferRequestDto.new Receiver();
        receiver.setAccount_number(selfTransferDTO.getToAccountNumber());
        String receiverCifNo = selfTransferDTO.getToAccountNumber().substring(3, 10);
        // below code commented for testing
        //Optional<AccountDetails> receiverAccountInfo = getTokenAndApiResponse(receiverCifNo);
        //String receiverCifNoAccName = receiverAccountInfo.get().getResponse().getPayload().getCustSummaryDetails().getIslamicAccounts().get(0).getAccdesc();
        //receiver.setAccount_name(receiverCifNoAccName);

        //set header
        TransferRequestDto.Header header = transferRequestDto.new Header();
        header.setSource_system("mpp-digital-app");
        header.setSource_user(selfTransferDTO.getUniqueKey());
        SourceOperation SourceOperation = sourceOperationRepository.findBySourceCode("SelfTransfer");
        header.setSource_operation(SourceOperation.getSourceOperation());

        //set transaction
        TransferRequestDto.Transaction transaction = transferRequestDto.new Transaction();
        TransferAccountFields TransferAccountFields = transferAccountFieldsRepository.findByTransferType("SELF TRANSFER");
        //shreya code required
        //transaction.setTransaction_reference();
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

        ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(response.getBody().getAccessToken());
            ObjectMapper objectMapper = JsonMapper.builder()
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .build();
            String jsonRequestBody = objectMapper.writeValueAsString(transferRequestDto);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequestBody, headers);
            ResponseEntity<TransferRequestDto> responseEntity = restTemplate.exchange(bankTransferUrl, HttpMethod.POST, requestEntity, TransferRequestDto.class);
            responseDTO.setStatus("Success");
            responseDTO.setMessage("Success");
            responseDTO.setData(responseEntity);
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




}