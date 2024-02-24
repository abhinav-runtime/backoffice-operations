package com.backoffice.operations.payloads;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardTransactionResponseDTO {
    List<CardTransaction> cardTransactions;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CardTransaction {

        private String transactionDescription;
        private Double transactionAmount;
        private LocalDate transactionDate;
        private String indicator;
        private String referenceNumber;

    }
}
