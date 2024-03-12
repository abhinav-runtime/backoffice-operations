package com.backoffice.operations.payloads;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditInfoRequestDto {

    private String customerNickName;
    private Boolean isAccountVisible;
    private Boolean isAlertOnTrnx;
    private Boolean isAlertOnLowBal;
    private double lowBalLimit;
    private String uniqueKey;
    private String accountNumber;
}
