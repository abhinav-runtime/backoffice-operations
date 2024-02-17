package com.backoffice.operations.service;

import com.backoffice.operations.exceptions.MaxResendAttemptsException;
import com.backoffice.operations.exceptions.OtpValidationException;
import com.backoffice.operations.payloads.OtpRequestDTO;
import com.backoffice.operations.payloads.SecuritySettingsDTO;
import com.backoffice.operations.payloads.ValidationResultDTO;

public interface OtpService {
	
	ValidationResultDTO validateOtp(OtpRequestDTO otpRequest) throws OtpValidationException;
	
	ValidationResultDTO resendOtp(String uniqueKeyCivilId) throws MaxResendAttemptsException;
    
    void saveSecuritySettings(SecuritySettingsDTO securitySettingsDTO);

}
