package com.backoffice.operations.service;

import com.backoffice.operations.exceptions.MaxResendAttemptsException;
import com.backoffice.operations.exceptions.OtpValidationException;
import com.backoffice.operations.payloads.SecuritySettingsDTO;

public interface OtpService {
	
	void validateOtp(String otp) throws OtpValidationException;
	
    void resendOtp(String userId) throws MaxResendAttemptsException;
    
    void saveSecuritySettings(SecuritySettingsDTO securitySettingsDTO);

}
