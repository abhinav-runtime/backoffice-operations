package com.backoffice.operations.service.impl;

import com.backoffice.operations.payloads.AccessTokenResponse;
import com.backoffice.operations.payloads.AccountDetails;
import com.backoffice.operations.payloads.AlizzTransferDto;
import com.backoffice.operations.payloads.AlizzTransferRequestDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
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

    public AlizzTransferServiceImpl(CommonUtils commonUtils, RestTemplate restTemplate, BeneficiaryService beneficiaryService) {
        this.commonUtils = commonUtils;
        this.restTemplate = restTemplate;
        this.beneficiaryService = beneficiaryService;
    }

    @Override
    public GenericResponseDTO<Object> transferToAlizzAccount(AlizzTransferRequestDto alizzTransferRequestDto) throws JsonProcessingException {

        ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
        if (Objects.nonNull(response.getBody())) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(response.getBody().getAccessToken());

            AlizzTransferDto alizzTransferDto = new AlizzTransferDto();
            AlizzTransferDto.Header header = new AlizzTransferDto.Header();
            header.setSource_system("");//DB
            header.setSource_user("");//DB
            header.setSource_operation("");//DB

            AlizzTransferDto.Transaction transaction = AlizzTransferDto.Transaction.builder()
                    .paymentDetails1(alizzTransferRequestDto.getNotesToReceiver()).paymentDetails2("")
                    .transactionReference("TRWA" + UUID.randomUUID()).transactionDate(LocalDate.now().toString())
                    .transactionAmount(alizzTransferRequestDto.getTransactionAmount())
                    .transactionPurpose(alizzTransferRequestDto.getTransactionPurpose()).transactionCurrency("OMR")//DB
                    .build();


            AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount senderAccDetails = getIslamicAccount(alizzTransferRequestDto.getFromAccountNumber());

            if(Objects.isNull(senderAccDetails)){
                return getErrorResponseGenericDTO(alizzTransferRequestDto, "Sender Account Invalid");
            }

            AccountDetails.Response.Payload.CustSummaryDetails.IslamicAccount receiverAccDetails = getIslamicAccount(alizzTransferRequestDto.getToAccountNumber());

            if(Objects.isNull(receiverAccDetails)){
                return getErrorResponseGenericDTO(alizzTransferRequestDto, "Receiver Account Invalid");
            }

            AlizzTransferDto.Sender sender = AlizzTransferDto.Sender.builder().accountNumber(senderAccDetails.getAcc())
                    .accountCurrency(senderAccDetails.getCcy()).accountName(senderAccDetails.getAdesc())
                    .branchCode("")//DB
                    .bankCode(alizzTransferRequestDto.getFromAccountNumber().substring(0, 3)).build();

            AlizzTransferDto.Receiver receiver = AlizzTransferDto.Receiver.builder()
                    .notesToReceiver(alizzTransferRequestDto.getNotesToReceiver())
                    .accountName(receiverAccDetails.getAdesc()).accountNumber(receiverAccDetails.getAcc()).build();

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
            ResponseEntity<AlizzTransferDto> responseEntity = restTemplate.exchange(alizzBankTransfer, HttpMethod.POST, requestEntity, AlizzTransferDto.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                beneficiaryService.addBeneficiary(alizzTransferDto.getReceiver());
            }
            GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
            Map<String, Object> data = new HashMap<>();
            data.put("alizzTransferDto", responseEntity.getBody());
            responseDTO.setStatus("Success");
            responseDTO.setMessage("Success");
            responseDTO.setData(data);
            return responseDTO;
        }
        return null;
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

//    GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
//        responseDTO.setStatus("Failure");
//        responseDTO.setMessage("No Entry Found");
//        responseDTO.setData(new HashMap<>());
//        return responseDTO;


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
