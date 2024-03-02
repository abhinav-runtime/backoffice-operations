package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlizzTransferRequestDto {

    private String transactionAmount;
    private String transactionPurpose;
    private String fromAccountNumber;
    private String toAccountNumber;
    private String notesToReceiver;
    private String uniqueKey;
}
