package com.backoffice.operations.payloads;

import com.backoffice.operations.entity.AccountType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountTypeDto {
    private String id;
    private String accountType;
    private String description;
    private String cbsProductCode;
    private String requestDebitCard;
    private String requestsChequeBook;
    private String billPayments;
    private String transfers;
    private String editAccountInfo;
    private String visibility;
}

