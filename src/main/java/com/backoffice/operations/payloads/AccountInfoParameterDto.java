package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfoParameterDto {
	int nickNameMinLength;
	int nickNameMaxLength;
	int noOfTimesUserCanChangeNickName;
	int makeAccountVisibleAttempts;
	int alertOnTransactions;
	int alertOnLowBalanceAttempts;
}
