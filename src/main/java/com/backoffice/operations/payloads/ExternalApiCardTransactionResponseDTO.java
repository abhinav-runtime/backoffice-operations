package com.backoffice.operations.payloads;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExternalApiCardTransactionResponseDTO {
    private List<Transaction> result;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Transaction {
        private long transactionDate;

        private String amount;

        private String crDr;

        private String merchantName;

        private String txId;
    }

}
