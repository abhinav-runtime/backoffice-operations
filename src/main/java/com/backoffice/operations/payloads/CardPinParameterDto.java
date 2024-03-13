package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardPinParameterDto {
	int pinLength;
	int cardPinMaximumAttempts;
	int sequentialDigits;
	int repetitiveDigits;
	int sessionExpiry;
	int incorrectPinAttempts;
	int cardPinCooldownInSec;
	int pinHistoryDepth;
}
