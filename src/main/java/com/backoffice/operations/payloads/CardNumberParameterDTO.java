package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardNumberParameterDTO {

	int cardFirstDigitLength;
	int cardLastDigitLength;
	int entryTimeOut;
	Boolean scanOption;
	int inputRetryLimit;
	int sessionExpireTime;
}
