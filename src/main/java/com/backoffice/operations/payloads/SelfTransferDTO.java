package com.backoffice.operations.payloads;

import lombok.Data;

@Data
public class SelfTransferDTO {

    private String fromAccountNumber;
    private String toAccountNumber;
    private double transactionAmount;
    private String notesToReceiver;
    private String uniqueKey;

}
