package com.backoffice.operations.service.impl;

import com.backoffice.operations.payloads.AccessTokenResponse;
import com.backoffice.operations.payloads.AccountDetails;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.ValidationService;
import com.backoffice.operations.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class ValidationServiceImpl implements ValidationService {

    private final CommonUtils commonUtils;

    @Value("${external.api.accounts}")
    private String accountExternalAPI;

    private final RestTemplate restTemplate;

    public ValidationServiceImpl(CommonUtils commonUtils, RestTemplate restTemplate) {
        this.commonUtils = commonUtils;
        this.restTemplate = restTemplate;
    }

    @Override
    public GenericResponseDTO<Object> validateUserAccount(String accountNumber) {
        String cifNumber = accountNumber.substring(3, 10);

        return getTokenAndApiResponse(cifNumber)
                .map(details -> details.getResponse().getPayload().getCustSummaryDetails().getIslamicAccounts()
                        .stream()
                        .anyMatch(accDetails -> accDetails.getAcc().equals(accountNumber)))
                .map(isValidUser -> {
                    GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
                    Map<String, Object> data = new HashMap<>();
                    data.put("accountNumber", accountNumber);
                    data.put("isValidUser", isValidUser);
                    data.put("message", isValidUser ? "Account is linked with the User" : "Account is not linked with the User");
                    responseDTO.setStatus("Success");
                    responseDTO.setMessage("Success");
                    responseDTO.setData(data);
                    return responseDTO;
                })
                .orElseGet(() -> {
                    GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
                    Map<String, Object> data = new HashMap<>();
                    data.put("accountNumber", accountNumber);
                    data.put("isValidUser", false);
                    data.put("message", "Invalid User");
                    responseDTO.setStatus("Success");
                    responseDTO.setMessage("Success");
                    responseDTO.setData(data);
                    return responseDTO;
                });
    }


    private Optional<AccountDetails> getTokenAndApiResponse(String civilId) {
        ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
        if (Objects.nonNull(response.getBody())) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(response.getBody().getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String apiUrl = accountExternalAPI + civilId;
            ResponseEntity<AccountDetails> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, AccountDetails.class);
            return Optional.ofNullable(responseEntity.getBody());
        }
        return Optional.empty();
    }
}
