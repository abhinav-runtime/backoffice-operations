package com.backoffice.operations.payloads;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    public static class Response{
        @JsonProperty("branch_code")
        public String branchCode;
        @JsonProperty("previous_working_day")
        public String previousWorkingDay;
        @JsonProperty("current_working_day")
        public String currentWorkingDay;
        @JsonProperty("next_working_day")
        public String nextWorkingDay;
    }
}
