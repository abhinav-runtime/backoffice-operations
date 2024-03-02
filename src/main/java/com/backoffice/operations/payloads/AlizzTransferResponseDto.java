package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlizzTransferResponseDto {

    private String accountName;
    private String accountNumber;
    private String bankName;
    private String transactionNumber;
    private String transactionDate;
}
