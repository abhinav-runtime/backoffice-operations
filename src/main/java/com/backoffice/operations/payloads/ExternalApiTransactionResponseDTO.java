package com.backoffice.operations.payloads;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExternalApiTransactionResponseDTO {

    private boolean success;
    private String message;
    private Response response;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private List<Transaction> transactions;

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class Transaction {

            private String indicator;
            private String transactionDesc;
            private double txnAmount;
            private String transactionDate;
            private String referenceNumber;
        }
    }
}
