package com.backoffice.operations.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
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
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.CivilIdRepository;
import com.backoffice.operations.repository.OtpParameterRepository;
import com.backoffice.operations.repository.OtpRepository;
import com.backoffice.operations.repository.SecuritySettingsRepository;
import com.backoffice.operations.service.OtpService;

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
	public GenericResponseDTO<Object> validateOtp(OtpRequestDTO otpRequest) throws OtpValidationException {		
		long id = 1;
		OtpParameter otpParameter = otpParameterRepository.findById(id).orElse(null);
		int otpMaxAttempts = otpParameter.getOtpMaxAttempts();

		Optional<CivilIdEntity> civilIdEntity = civilIdRepository.findById(otpRequest.getUniqueKey());
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
//		ValidationResultDTO validationResultDTO = new ValidationResultDTO();
//		ValidationResultDTO.Data data = new ValidationResultDTO.Data();
		if (civilIdEntity.isPresent()) {
			OtpEntity otpEntity = new OtpEntity();
			otpEntity.setOtp("1234");
				
			if (otpRequest.getOtp() == null) {
				Map<String, String> data = new HashMap<>();
				data.put("uniqueKey", civilIdEntity.get().getId().toString());
				responseDTO.setStatus("Failure");
				responseDTO.setMessage("Invalid OTP or OTP expired");
				responseDTO.setData(data);
				return responseDTO;
			}

			if (!otpEntity.getOtp().equals(otpRequest.getOtp())) {
				otpEntity.setAttempts(otpEntity.getAttempts() + 1);
				otpEntity.setLastAttemptTime(LocalDateTime.now());
				otpRepository.save(otpEntity);

				if (otpEntity.getAttempts() >= otpMaxAttempts) {
           Map<String, String> data = new HashMap<>();
					responseDTO.setStatus("Failure");
					responseDTO.setMessage("Maximum attempts reached. Please try again later.");
					data.put("uniqueKey",civilIdEntity.get().getId().toString());
					responseDTO.setData(data);
					return responseDTO;
				} else {
					Map<String, String> data = new HashMap<>();
					responseDTO.setStatus("Failure");
					responseDTO.setMessage("Invalid OTP. Attempts left: " + (otpMaxAttempts - otpEntity.getAttempts()));
					data.put("uniqueKey", civilIdEntity.get().getId().toString());
					responseDTO.setData(data);
					return responseDTO;
				}
			}

			// Check expiration time (e.g., within the last 10 minutes)
			LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(10);
			if (Objects.nonNull(otpEntity.getLastAttemptTime())
					&& otpEntity.getLastAttemptTime().isBefore(expirationTime)) {
				Map<String, String> data = new HashMap<>();
				responseDTO.setStatus("Failure");
				responseDTO.setMessage("OTP expired. Please request a new OTP.");
				data.put("uniqueKey", civilIdEntity.get().getId().toString());
				responseDTO.setData(data);
				return responseDTO;
			}

			// OTP validation successful, reset attempts
			otpEntity.setAttempts(0);
			otpEntity.setLastAttemptTime(LocalDateTime.now());
			otpRepository.save(otpEntity);
			Map<String, String> data = new HashMap<>();
			responseDTO.setStatus("Success");
			responseDTO.setMessage("Success");
			data.put("uniqueKey", civilIdEntity.get().getId().toString());
			responseDTO.setData(data);
			return responseDTO;
		}
		 
		return null;
	}

	@Override
	public GenericResponseDTO<Object> resendOtp(String uniqueKey) throws MaxResendAttemptsException {
		long id = 1;
		OtpParameter otpParameter = otpParameterRepository.findById(id).orElse(null);
		int cooldownPeriodMinutes = otpParameter.getOtpCooldownInMin();
		OtpEntity otpEntity = otpRepository.findByUniqueKeyCivilId(uniqueKey);
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
//		ValidationResultDTO validationResultDTO = new ValidationResultDTO();
//		ValidationResultDTO.Data data = new ValidationResultDTO.Data();
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
				Map<String, String> data = new HashMap<>();
				responseDTO.setStatus("Failure");
				responseDTO.setMessage("Resend attempts exceeded. Please try again later.");
				data.put("uniqueKey", uniqueKey);
				responseDTO.setData(data);
				return responseDTO;
			}
				
			// Reset attempts and generate a new OTP
			otpEntity.setAttempts(0);
			generateAndSaveOtp(otpEntity);
		}
    
		Map<String, String> data = new HashMap<>();
		responseDTO.setStatus("Success");
		responseDTO.setMessage("Success");
		data.put("uniqueKey", uniqueKey);
		responseDTO.setData(data);
		return responseDTO;

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
