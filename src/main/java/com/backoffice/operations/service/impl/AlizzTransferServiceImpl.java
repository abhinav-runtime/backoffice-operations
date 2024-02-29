package com.backoffice.operations.service.impl;

import com.backoffice.operations.payloads.AccessTokenResponse;
import com.backoffice.operations.payloads.AlizzTransferDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.AlizzTransferService;
import com.backoffice.operations.service.BeneficiaryService;
import com.backoffice.operations.utils.CommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class AlizzTransferServiceImpl implements AlizzTransferService {


    @Value("${external.api.transfer.bank}")
    private String alizzBankTransfer;

    private final CommonUtils commonUtils;

    private final RestTemplate restTemplate;

    private final BeneficiaryService beneficiaryService;

    public AlizzTransferServiceImpl(CommonUtils commonUtils, RestTemplate restTemplate, BeneficiaryService beneficiaryService) {
        this.commonUtils = commonUtils;
        this.restTemplate = restTemplate;
        this.beneficiaryService = beneficiaryService;
    }

    @Override
    public GenericResponseDTO<Object> transferToAlizzAccount(AlizzTransferDto alizzTransferDto) throws JsonProcessingException {

        ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
        if (Objects.nonNull(response.getBody())) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(response.getBody().getAccessToken());

            AlizzTransferDto.Header header = new AlizzTransferDto.Header();
            header.setSource_system("mpp-digital-app");
            header.setSource_user("Aditya");
            header.setSource_operation("Fund Transfer");
            alizzTransferDto.setHeader(header);

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
        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
        responseDTO.setStatus("Failure");
        responseDTO.setMessage("No Entry Found");
        responseDTO.setData(new HashMap<>());
        return responseDTO;
    }
}
