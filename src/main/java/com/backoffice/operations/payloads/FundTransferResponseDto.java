package com.backoffice.operations.payloads;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class FundTransferResponseDto {
    private boolean success;
    private String message;
    Response response;



    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
    Result result;
}

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
    CstmrCdtTrfInitn cstmrCdtTrfInitn;
    ArrayList<Object> fcubserrorresp = new ArrayList<>();
    ArrayList<Object> fcubswarningresp = new ArrayList<>();

}

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CstmrCdtTrfInitn {
    GrpTlr grpTlr;

}

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GrpTlr {
    private Long txnRefNo;
}


}
