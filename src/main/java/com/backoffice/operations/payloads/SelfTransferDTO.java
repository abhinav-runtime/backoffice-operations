package com.backoffice.operations.payloads;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SelfTransferDTO {

    private String fromAccountNumber;
    private String toAccountNumber;
    private double transactionAmount;
    private String notesToReceiver;
    private String uniqueKey;
    private String customerType;
    private String transactionType;

}
