package com.backoffice.operations.utils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
@Component
public class ApiCaller {

    @Value("${external.api.check.available.balance}")
    private String apiUrl;

    // Method to fetch avlbal from the API response
    public double getAvailableBalance(String accountNumber) {
        // Define the API URL
        RestTemplate restTemplate = new RestTemplate();
        ApiResponse response = restTemplate.getForObject(apiUrl + accountNumber, ApiResponse.class);
        // Check if the response is successful and contains payload
        if (response != null && response.isSuccess() && response.getResponse() != null) {
            // Extract avlbal from the response payload
            return response.getResponse().getPayload().getAccbalance().getAccbal().get(0).getAvlbal();
        } else {
            return -1; // or throw an exception
        }
    }
}

// Define classes to map API response JSON
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
