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
    Response ResponseObject;



    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class Response {
    Result ResultObject;
}

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class Result {
    CstmrCdtTrfInitn CstmrCdtTrfInitnObject;
    ArrayList<Object> fcubserrorresp = new ArrayList<Object>();
    ArrayList<Object> fcubswarningresp = new ArrayList<Object>();

}

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class CstmrCdtTrfInitn {
    GrpTlr GrpTlrObject;

}

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class GrpTlr {
    private Integer txnRefNo;
}


}
