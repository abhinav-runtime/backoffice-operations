package com.backoffice.operations.service.impl;

import com.backoffice.operations.payloads.AccessTokenResponse;
import com.backoffice.operations.payloads.TransferRequestDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.TransferService;
import com.backoffice.operations.utils.CommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransferServiceImpl implements TransferService {

    private final CommonUtils commonUtils;

    private final RestTemplate restTemplate;
    public TransferServiceImpl(CommonUtils commonUtils, RestTemplate restTemplate) {
        this.commonUtils = commonUtils;
        this.restTemplate = restTemplate;
    }

    @Value("${bank.transfer.url}")
    private String bankTransferUrl;


    @Override
    public GenericResponseDTO<Object> transferToBank(TransferRequestDto transferRequest) throws JsonProcessingException {
        ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(response.getBody().getAccessToken());
            ObjectMapper objectMapper = JsonMapper.builder()
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .build();
            String jsonRequestBody = objectMapper.writeValueAsString(transferRequest);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequestBody, headers);
            ResponseEntity<TransferRequestDto> responseEntity = restTemplate.exchange(bankTransferUrl, HttpMethod.POST, requestEntity, TransferRequestDto.class);
            responseDTO.setStatus("Success");
            responseDTO.setMessage("Success");
            responseDTO.setData(responseEntity);
            return responseDTO;
    }
}