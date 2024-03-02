package com.backoffice.operations.service.impl;

import com.backoffice.operations.payloads.AccessTokenResponse;
import com.backoffice.operations.payloads.AccountDetails;
import com.backoffice.operations.payloads.SelfTransferDTO;
import com.backoffice.operations.payloads.TransferRequestDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.TransferService;
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
    public TransferServiceImpl(CommonUtils commonUtils, RestTemplate restTemplate) {
        this.commonUtils = commonUtils;
        this.restTemplate = restTemplate;
    }

    @Value("${bank.transfer.url}")
    private String bankTransferUrl;


    @Override
    public GenericResponseDTO<Object> transferToBank(SelfTransferDTO selfTransferDTO) throws JsonProcessingException {

        TransferRequestDto transferRequestDto = new TransferRequestDto();

        //Sender details
        TransferRequestDto.Sender sender = transferRequestDto.new Sender();
        sender.setAccount_number(selfTransferDTO.getFromAccountNumber());
        String senderCifNo = selfTransferDTO.getFromAccountNumber().substring(3, 10);
        Optional<AccountDetails> senderAccountInfo = getTokenAndApiResponse(senderCifNo);
        String senderAccName = senderAccountInfo.get().getResponse().getPayload().getCustSummaryDetails().getIslamicAccounts().get(0).getAccdesc();
        sender.setAccount_name(senderAccName);

        //Receiver details
        TransferRequestDto.Receiver receiver = transferRequestDto.new Receiver();
        receiver.setAccount_number(selfTransferDTO.getToAccountNumber());
        String receiverCifNo = selfTransferDTO.getToAccountNumber().substring(3, 10);
        Optional<AccountDetails> receiverAccountInfo = getTokenAndApiResponse(receiverCifNo);
        String receiverCifNoAccName = receiverAccountInfo.get().getResponse().getPayload().getCustSummaryDetails().getIslamicAccounts().get(0).getAccdesc();
        receiver.setAccount_name(receiverCifNoAccName);

        TransferRequestDto.Header header = transferRequestDto.new Header();
        header.setSource_system("mpp-digital-app");
        header.setSource_user(selfTransferDTO.getUniqueKey());





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