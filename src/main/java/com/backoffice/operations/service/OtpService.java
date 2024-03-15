package com.backoffice.operations.service;

import com.backoffice.operations.exceptions.MaxResendAttemptsException;
import com.backoffice.operations.exceptions.OtpValidationException;
import com.backoffice.operations.payloads.OtpRequestDTO;
import com.backoffice.operations.payloads.SecuritySettingsDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;

public interface OtpService {
	
	GenericResponseDTO<Object> validateOtp(OtpRequestDTO otpRequest, String token) throws OtpValidationException;

	GenericResponseDTO<Object> transferOTP(String uniqueKey, String otp) throws OtpValidationException;
	
	GenericResponseDTO<Object> resendOtp(String uniqueKeyCivilId) throws MaxResendAttemptsException;
    
    void saveSecuritySettings(SecuritySettingsDTO securitySettingsDTO);

}
