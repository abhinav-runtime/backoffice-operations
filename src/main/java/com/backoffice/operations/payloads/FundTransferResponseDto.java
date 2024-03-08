package com.backoffice.operations.payloads;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FundTransferResponseDto {
    private boolean success;
    private String message;
    Response ResponseObject;



    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Response {
    Result ResultObject;
}

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Result {
    CstmrCdtTrfInitn CstmrCdtTrfInitnObject;
    ArrayList<Object> fcubserrorresp = new ArrayList<>();
    ArrayList<Object> fcubswarningresp = new ArrayList<>();

}

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class CstmrCdtTrfInitn {
    GrpTlr GrpTlrObject;

}

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class GrpTlr {
    private Integer txnRefNo;
}


}
