package com.backoffice.operations.service;

import com.backoffice.operations.exceptions.MaxResendAttemptsException;
import com.backoffice.operations.exceptions.OtpValidationException;
import com.backoffice.operations.payloads.OtpRequestDTO;
import com.backoffice.operations.payloads.SecuritySettingsDTO;

public interface OtpService {
	
	void validateOtp(OtpRequestDTO otpRequest) throws OtpValidationException;
	
    String resendOtp(String uniqueKeyCivilId) throws MaxResendAttemptsException;
    
    void saveSecuritySettings(SecuritySettingsDTO securitySettingsDTO);

}
