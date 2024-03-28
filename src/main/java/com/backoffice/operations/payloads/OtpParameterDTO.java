package com.backoffice.operations.payloads;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtpParameterDTO {

	int otpValidity;
	int otpMaxAttempts;
	int attemptTimeOut;
	String otpLang;
	int otpLength;
	String allowedChannels;
	int otpResend;
	int otpResentTime;
	int sessionExpiry;
}
