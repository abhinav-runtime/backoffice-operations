package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PassCodeParameterDTO {
	int passcodeLength;
	int passCodeMaxAttempt;
	int lockoutDurationInMin;
	int changePasscodeMaxAttempt;
	int changePasscodeDurationInDays;
}
