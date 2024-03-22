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

    private Double transactionAmount;
    private String transactionPurpose;
    private String fromAccountNumber;
    private String toAccountNumber;
    private String notesToReceiver;
    private String uniqueKey;
    private String toAccountName;
    private String toBankName;
    private String otp;
}
