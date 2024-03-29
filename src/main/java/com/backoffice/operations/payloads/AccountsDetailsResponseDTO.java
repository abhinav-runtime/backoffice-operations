package com.backoffice.operations.payloads;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountsDetailsResponseDTO {

    private String accountNumber;
    private String currency;
    private Double accountBalance;
    private String accountType;
    private String accountCodeDesc;
    private String type;
    private String accountNickName;
    private String shariaContract;
    private String openDate;
    private String branchName;
    private String jointType;
    private String customerName;
    private Double blockedAmount;
    private Double currentAccountBalance;
    private boolean isAccountVisible;
    private boolean isAlertOnTrnx;
    private boolean isAlertOnLowBal;
    private double lowBalLimit;
    private String customerNickName;
    private String requestDebitCard;
    private String requestsChequeBook;
    private String billPayments;
    private String transfers;
    private String editAccountInfo;
    private String visibility;
    private String color;
    private String customerType;
}
