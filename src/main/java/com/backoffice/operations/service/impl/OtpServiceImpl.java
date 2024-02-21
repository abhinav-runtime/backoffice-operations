package com.backoffice.operations.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.CardPinParameter;
import com.backoffice.operations.entity.CivilIdEntity;
import com.backoffice.operations.entity.OtpEntity;
import com.backoffice.operations.entity.OtpParameter;
import com.backoffice.operations.entity.SecuritySettings;
import com.backoffice.operations.exceptions.MaxResendAttemptsException;
import com.backoffice.operations.exceptions.OtpValidationException;
import com.backoffice.operations.payloads.OtpRequestDTO;
import com.backoffice.operations.payloads.SecuritySettingsDTO;
import com.backoffice.operations.payloads.ValidationResultDTO;
import com.backoffice.operations.repository.CivilIdRepository;
import com.backoffice.operations.repository.OtpParameterRepository;
import com.backoffice.operations.repository.OtpRepository;
import com.backoffice.operations.repository.SecuritySettingsRepository;
import com.backoffice.operations.service.OtpService;
import com.backoffice.operations.utils.CommonUtils;

@Service
public class OtpServiceImpl implements OtpService {
	@Autowired
	private OtpRepository otpRepository;

	@Autowired
	private SecuritySettingsRepository securitySettingsRepository;

	@Autowired
	private CivilIdRepository civilIdRepository;
	
	@Autowired
	private OtpParameterRepository otpParameterRepository;
	

	@Override
	public ValidationResultDTO validateOtp(OtpRequestDTO otpRequest) throws OtpValidationException {
		long id = 1;
		OtpParameter otpParameter = otpParameterRepository.findById(id).orElse(null);
		int otpMaxAttempts = otpParameter.getOtpMaxAttempts();

		
		Optional<CivilIdEntity> civilIdEntity = civilIdRepository.findById(otpRequest.getUniqueKey());
		ValidationResultDTO validationResultDTO = new ValidationResultDTO();
		ValidationResultDTO.Data data = new ValidationResultDTO.Data();
		if (civilIdEntity.isPresent()) {
			OtpEntity otpEntity = new OtpEntity();
			otpEntity.setOtp("1234");
				
			if (otpRequest.getOtp() == null) {

				validationResultDTO.setStatus("Failure");
				validationResultDTO.setMessage("Invalid OTP or OTP expired");
				data.setUniqueKey(civilIdEntity.get().getId().toString());
				validationResultDTO.setData(data);
				return validationResultDTO;
			}

			if (!otpEntity.getOtp().equals(otpRequest.getOtp())) {
				otpEntity.setAttempts(otpEntity.getAttempts() + 1);
				otpEntity.setLastAttemptTime(LocalDateTime.now());
				otpRepository.save(otpEntity);

				if (otpEntity.getAttempts() >= otpMaxAttempts) {
					validationResultDTO.setStatus("Failure");
					validationResultDTO.setMessage("Maximum attempts reached. Please try again later.");
					data.setUniqueKey(civilIdEntity.get().getId().toString());
					validationResultDTO.setData(data);
					return validationResultDTO;
				} 	
					validationResultDTO.setStatus("Failure");
					validationResultDTO
							.setMessage("Invalid OTP. Attempts left: " + (otpMaxAttempts - otpEntity.getAttempts()));
					data.setUniqueKey(civilIdEntity.get().getId().toString());
					validationResultDTO.setData(data);
					return validationResultDTO;
				
			}

			// Check expiration time (e.g., within the last 10 minutes)
			LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(10);
			if (Objects.nonNull(otpEntity.getLastAttemptTime())
					&& otpEntity.getLastAttemptTime().isBefore(expirationTime)) {
				validationResultDTO.setStatus("Failure");
				validationResultDTO.setMessage("OTP expired. Please request a new OTP.");
				data.setUniqueKey(civilIdEntity.get().getId().toString());
				validationResultDTO.setData(data);
				return validationResultDTO;
			}

			// OTP validation successful, reset attempts
			otpEntity.setAttempts(0);
			otpEntity.setLastAttemptTime(LocalDateTime.now());
			otpRepository.save(otpEntity);

			validationResultDTO.setStatus("Success");
			validationResultDTO.setMessage("Success");
			data.setUniqueKey(civilIdEntity.get().getId().toString());
			validationResultDTO.setData(data);
			return validationResultDTO;
		}
		 
		return null;
	}

	@Override
	public ValidationResultDTO resendOtp(String uniqueKey) throws MaxResendAttemptsException {
		
		long id = 1;
		OtpParameter otpParameter = otpParameterRepository.findById(id).orElse(null);
		int cooldownPeriodMinutes = otpParameter.getOtpCooldownInMin();
		
		OtpEntity otpEntity = otpRepository.findByUniqueKeyCivilId(uniqueKey);
		ValidationResultDTO validationResultDTO = new ValidationResultDTO();
		ValidationResultDTO.Data data = new ValidationResultDTO.Data();
		// TODO: return how many attempts
		if (otpEntity == null) {
			// First-time request, generate and save a new OTP
			otpEntity = new OtpEntity();
			otpEntity.setUniqueKeyCivilId(uniqueKey);
			generateAndSaveOtp(otpEntity);
		} else {
			// Check cooldown period
			LocalDateTime cooldownEndTime = otpEntity.getLastAttemptTime().plusMinutes(cooldownPeriodMinutes);
			if (LocalDateTime.now().isBefore(cooldownEndTime)) {
				validationResultDTO.setStatus("Failure");
				validationResultDTO.setMessage("Resend attempts exceeded. Please try again later.");
				data.setUniqueKey(uniqueKey);
				validationResultDTO.setData(data);
				return validationResultDTO;
			}
				
			// Reset attempts and generate a new OTP
			otpEntity.setAttempts(0);
			generateAndSaveOtp(otpEntity);
		}
		
		
		validationResultDTO.setStatus("Success");
		validationResultDTO.setMessage("Success");
		data.setUniqueKey(uniqueKey);
		validationResultDTO.setData(data);
		return validationResultDTO;
	}

	@Override
	public void saveSecuritySettings(SecuritySettingsDTO securitySettingsDTO) {
		SecuritySettings securitySettings = new SecuritySettings();
		if (Objects.nonNull(securitySettingsDTO)) {
			if (Objects.nonNull(securitySettingsDTO.isBiometricEnabled()))
				securitySettings.setBiometricEnabled(securitySettingsDTO.isBiometricEnabled());
			if (Objects.nonNull(securitySettingsDTO.isFaceIdEnabled()))
				securitySettings.setFaceIdEnabled(securitySettingsDTO.isFaceIdEnabled());
			if (Objects.nonNull(securitySettingsDTO.isPasscodeEnabled()))
				securitySettings.setPasscodeEnabled(securitySettingsDTO.isPasscodeEnabled());
			if (Objects.nonNull(securitySettingsDTO.isPinEnabled()))
				securitySettings.setPinEnabled(securitySettingsDTO.isPinEnabled());
			securitySettingsRepository.save(securitySettings);
		}
	}

	private void generateAndSaveOtp(OtpEntity otpEntity) {
//		String newOtp = CommonUtils.generateRandomOtp();
		otpEntity.setOtp("1234");
		otpEntity.setLastAttemptTime(LocalDateTime.now());
		otpRepository.save(otpEntity);
		// Send the OTP to the user (e.g., via SMS, email, etc.)
	}

}
