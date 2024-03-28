package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileParameterDto {
	int mobileChangeAttemptLimit;
	int mobileChangeLockoutDurationInSec;
	int mobileNumberChangedLockoutDurationInDay;
	int emailIdChangeAttemptLimit;
	int emailIdVerificationLinkResendAttempts;
	int emailIdVerificationLinkTimeoutInSec;
	int emailIdChangeLockoutDurationInSec;
	int emailIdChangedDurationInDay;
	int emailStatementFlagAttempts;
}
