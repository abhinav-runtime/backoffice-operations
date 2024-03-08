package com.backoffice.operations.payloads;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundTransferResponseDto {
    private boolean success;
    private String message;
    Response response;



    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
    Result result;
}

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Result {
    CstmrCdtTrfInitn cstmrCdtTrfInitn;
    ArrayList<Object> fcubserrorresp = new ArrayList<>();
    ArrayList<Object> fcubswarningresp = new ArrayList<>();

}

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CstmrCdtTrfInitn {
    GrpTlr grpTlr;

}

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GrpTlr {
    private Integer txnRefNo;
}


}
