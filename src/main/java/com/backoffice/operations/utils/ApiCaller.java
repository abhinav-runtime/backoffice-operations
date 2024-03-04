package com.backoffice.operations.utils;
import com.backoffice.operations.payloads.AccessTokenResponse;
import com.backoffice.operations.payloads.CivilIdAPIResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Component
public class ApiCaller {

    @Value("${external.api.check.available.balance}")
    private String apiUrl;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    @Qualifier("jwtAuth")
    private RestTemplate jwtAuthRestTemplate;

    // Method to fetch avlbal from the API response
    public double getAvailableBalance(String accountNumber) {
        ResponseEntity<AccessTokenResponse> tokenResponse = commonUtils.getToken();
        if (Objects.nonNull(tokenResponse.getBody())) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(tokenResponse.getBody().getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<ApiResponse> response = jwtAuthRestTemplate.exchange(apiUrl + accountNumber, HttpMethod.GET, entity,
                    ApiResponse.class);
            // Check if the response is successful and contains payload
            if (Objects.nonNull(response) && Objects.nonNull(response.getBody()) && response.getBody().isSuccess() && response.getBody().getResponse() != null) {
                // Extract avlbal from the response payload
                return response.getBody().getResponse().getPayload().getAccbalance().getAccbal().get(0).getAvlbal();
            }
        }
        return -1;
    }
}

@Data
class ApiResponse {
    private boolean success;
    private ResponseData response;

    public boolean isSuccess() {
        return success;
    }


}
@Data
class ResponseData {
    private Payload payload;

}
@Data
class Payload {
    private AccountBalance accbalance;

}
@Data
class AccountBalance {
    private List<AccountDetails> accbal;

}
@Data
class AccountDetails {
    private double avlbal;

}
