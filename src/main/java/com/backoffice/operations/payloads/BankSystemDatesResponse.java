package com.backoffice.operations.payloads;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankSystemDatesResponse {
    public boolean success;
    public String message;
    public Response response;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response{
        public String branchCode;
        public String previousWorkingDay;
        public String currentWorkingDay;
        public String nextWorkingDay;
    }
}
