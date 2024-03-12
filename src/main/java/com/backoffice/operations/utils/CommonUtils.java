package com.backoffice.operations.utils;

import com.backoffice.operations.payloads.AccessTokenResponse;
import com.backoffice.operations.payloads.BankSystemDatesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Random;

@Component
public class CommonUtils {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    @Value("${external.api.m2p.token}")
    private String tokenApiUrl;

    @Value("${external.api.check.bank.dates}")
    private String bankDatesUrl;

    @Autowired
    @Qualifier("jwtAuth")
    private RestTemplate jwtAuthRestTemplate;

    public static String generateRandomOtp() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    public ResponseEntity<AccessTokenResponse> getToken() {
        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Set request body parameters
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "client_credentials");
        requestBody.add("scope", "openid profile email");
        requestBody.add("client_id", "mpp-digital-app");
        requestBody.add("client_secret", "gh2KpMeNlwuSZJBQqCyhRbnRh0BHQwCW");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        logger.info("requestUrl: {}", tokenApiUrl);
        logger.info("requestEntity: {}", requestEntity);
        ResponseEntity<AccessTokenResponse> response = jwtAuthRestTemplate.exchange(tokenApiUrl, HttpMethod.POST, requestEntity, AccessTokenResponse.class);
        logger.info("response: {}", response);
        return response;
    }

    public String getBankDate(String branchCode) {
        ResponseEntity<AccessTokenResponse> response = getToken();
        if (Objects.nonNull(response.getBody())) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(response.getBody().getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String apiUrl = bankDatesUrl + branchCode;
            ResponseEntity<BankSystemDatesResponse> responseEntity = jwtAuthRestTemplate.exchange(apiUrl, HttpMethod.GET, entity, BankSystemDatesResponse.class);
            BankSystemDatesResponse responseObject = responseEntity.getBody();
            return Objects.nonNull(responseObject) ? responseObject.getResponse().getCurrentWorkingDay() : "";
        }
        return "";
    }

}
