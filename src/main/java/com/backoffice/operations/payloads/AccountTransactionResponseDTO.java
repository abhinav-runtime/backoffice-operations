package com.backoffice.operations.payloads;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountTransactionResponseDTO {
    List<AccountTransaction> accountTransactions;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AccountTransaction {

        private String transactionDescription;
        private Double transactionAmount;
        private LocalDate transactionDate;
        private String indicator;
        private String referenceNumber;
        private String transactionFrom;
        private Double availableBalance;
        private String transactionTo;

    }
}
