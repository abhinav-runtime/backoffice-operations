package com.backoffice.operations.service.impl;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.backoffice.operations.entity.OtpEntity;
import com.backoffice.operations.entity.SecuritySettings;
import com.backoffice.operations.exceptions.MaxResendAttemptsException;
import com.backoffice.operations.exceptions.OtpValidationException;
import com.backoffice.operations.payloads.SecuritySettingsDTO;
import com.backoffice.operations.repository.OtpRepository;
import com.backoffice.operations.repository.SecuritySettingsRepository;
import com.backoffice.operations.service.OtpService;

@Service
public class OtpServiceImpl implements OtpService {
    @Autowired
    private OtpRepository otpRepository;
    
    @Value("${otp.maxAttempts}")
    private int otpMaxAttempts;
    
    @Value("${otp.cooldownPeriodMinutes}")
    private int cooldownPeriodMinutes;
    
    @Autowired
    private SecuritySettingsRepository securitySettingsRepository;

    // Maximum allowed attempts for OTP validation
//    private static final int MAX_ATTEMPTS = 5;

    // Cooldown period in minutes before allowing another OTP resend
//    private static final int COOLDOWN_PERIOD_MINUTES = 5;

    @Override
    public void validateOtp(String otp) throws OtpValidationException {
    	OtpEntity otpEntity = new OtpEntity();
    	otpEntity.setOtp("1234");
        if (otp == null) {
            throw new OtpValidationException("Invalid OTP or OTP expired");
        }

        if (!otpEntity.getOtp().equals(otp)) {
            otpEntity.setAttempts(otpEntity.getAttempts() + 1);
            otpEntity.setLastAttemptTime(LocalDateTime.now());
            otpRepository.save(otpEntity);

            if (otpEntity.getAttempts() >= otpMaxAttempts) {
                throw new OtpValidationException("Maximum attempts reached. Please try again later.");
            } else {
                throw new OtpValidationException("Invalid OTP. Attempts left: " + (otpMaxAttempts - otpEntity.getAttempts()));
            }
        }

        // Check expiration time (e.g., within the last 10 minutes)
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(10);
        if (!StringUtils.isEmpty(otpEntity.getLastAttemptTime()) && otpEntity.getLastAttemptTime().isBefore(expirationTime)) {
            throw new OtpValidationException("OTP expired. Please request a new OTP.");
        }

        // OTP validation successful, reset attempts
        otpEntity.setAttempts(0);
        otpEntity.setLastAttemptTime(LocalDateTime.now());
        otpRepository.save(otpEntity);
    }

    @Override
    public void resendOtp(String userId) throws MaxResendAttemptsException {
        OtpEntity otpEntity = otpRepository.findByUserId(userId);

        if (otpEntity == null) {
            // First-time request, generate and save a new OTP
            otpEntity = new OtpEntity();
            otpEntity.setUserId(userId);
            generateAndSaveOtp(otpEntity);
        } else {
            // Check cooldown period
            LocalDateTime cooldownEndTime = otpEntity.getLastAttemptTime().plusMinutes(cooldownPeriodMinutes);
            if (LocalDateTime.now().isBefore(cooldownEndTime)) {
                throw new MaxResendAttemptsException("Resend attempts exceeded. Please try again later.");
            }

            // Reset attempts and generate a new OTP
            otpEntity.setAttempts(0);
            generateAndSaveOtp(otpEntity);
        }
    }
    
    @Override
    public void saveSecuritySettings(SecuritySettingsDTO securitySettingsDTO) {
        SecuritySettings securitySettings = new SecuritySettings();
        if(Objects.nonNull(securitySettingsDTO)) {
        	if(Objects.nonNull(securitySettingsDTO.isBiometricEnabled()))
        		securitySettings.setBiometricEnabled(securitySettingsDTO.isBiometricEnabled());
        	if(Objects.nonNull(securitySettingsDTO.isTouchIdEnabled()))
        		securitySettings.setTouchIdEnabled(securitySettingsDTO.isTouchIdEnabled());
        	if(Objects.nonNull(securitySettingsDTO.isPasscodeEnabled()))
        		securitySettings.setPasscodeEnabled(securitySettingsDTO.isPasscodeEnabled());
        	if(Objects.nonNull(securitySettingsDTO.isPinEnabled()))
        		securitySettings.setPinEnabled(securitySettingsDTO.isPinEnabled());	
            securitySettingsRepository.save(securitySettings);
        }
    }

    private void generateAndSaveOtp(OtpEntity otpEntity) {
        // Generate a new OTP (you can use your own logic for OTP generation)
        String newOtp = generateRandomOtp();
        otpEntity.setOtp(newOtp);
        otpEntity.setLastAttemptTime(LocalDateTime.now());
        otpRepository.save(otpEntity);
        // Send the OTP to the user (e.g., via SMS, email, etc.)
    }

    private String generateRandomOtp() {
        // Implement your logic to generate a random OTP (e.g., a 6-digit code)
        // This is a simple example; in a real scenario, you may want to use a stronger OTP generation method.
        return String.format("%06d", new Random().nextInt(1000000));
    }
}

