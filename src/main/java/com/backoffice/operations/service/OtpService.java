package com.backoffice.operations.service;

import com.backoffice.operations.entity.SecuritySettings;
import com.backoffice.operations.exceptions.MaxResendAttemptsException;
import com.backoffice.operations.exceptions.OtpValidationException;
import com.backoffice.operations.payloads.OtpRequestDTO;
import com.backoffice.operations.payloads.SecuritySettingsDTO;
import com.backoffice.operations.payloads.ValidationResultDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.payloads.common.InnerData;

public interface OtpService {

	GenericResponseDTO<InnerData> validateOtp(OtpRequestDTO otpRequest) throws OtpValidationException;
	
	ValidationResultDTO resendOtp(String uniqueKeyCivilId) throws MaxResendAttemptsException;

	GenericResponseDTO<SecuritySettings> saveSecuritySettings(SecuritySettingsDTO securitySettingsDTO);

}
