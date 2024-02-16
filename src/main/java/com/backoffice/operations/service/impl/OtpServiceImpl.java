package com.backoffice.operations.service.impl;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.payloads.common.InnerData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.CivilIdEntity;
import com.backoffice.operations.entity.OtpEntity;
import com.backoffice.operations.entity.SecuritySettings;
import com.backoffice.operations.exceptions.MaxResendAttemptsException;
import com.backoffice.operations.exceptions.OtpValidationException;
import com.backoffice.operations.payloads.OtpRequestDTO;
import com.backoffice.operations.payloads.SecuritySettingsDTO;
import com.backoffice.operations.payloads.ValidationResultDTO;
import com.backoffice.operations.repository.CivilIdRepository;
import com.backoffice.operations.repository.OtpRepository;
import com.backoffice.operations.repository.SecuritySettingsRepository;
import com.backoffice.operations.service.OtpService;
import com.backoffice.operations.utils.CommonUtils;

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

	@Autowired
	private CivilIdRepository civilIdRepository;

	// Maximum allowed attempts for OTP validation
//    private static final int MAX_ATTEMPTS = 5;

	// Cooldown period in minutes before allowing another OTP resend
//    private static final int COOLDOWN_PERIOD_MINUTES = 5;

	@Override
	public GenericResponseDTO<InnerData> validateOtp(OtpRequestDTO otpRequest) throws OtpValidationException {
		Optional<CivilIdEntity> civilIdEntity = civilIdRepository.findById(otpRequest.getUniqueKey());
		GenericResponseDTO<InnerData> genericResult = new GenericResponseDTO<>();
		//ValidationResultDTO validationResultDTO = new ValidationResultDTO();
		//ValidationResultDTO.Data data = new ValidationResultDTO.Data();
		if (civilIdEntity.isPresent()) {
			OtpEntity otpEntity = new OtpEntity();
			otpEntity.setOtp("1234");

			if (otpRequest.getOtp() == null) {
				genericResult.setStatus("Failure");
				genericResult.setMessage("Invalid OTP or OTP expired");
				//data.setUniqueKey(civilIdEntity.get().getId().toString());
				String uniqueKey = civilIdEntity.get().getId().toString();
				genericResult.setData(new InnerData(uniqueKey));
				return genericResult;
			}

			if (!otpEntity.getOtp().equals(otpRequest.getOtp())) {
				otpEntity.setAttempts(otpEntity.getAttempts() + 1);
				otpEntity.setLastAttemptTime(LocalDateTime.now());
				otpRepository.save(otpEntity);

				if (otpEntity.getAttempts() >= otpMaxAttempts) {
					genericResult.setStatus("Failure");
					genericResult.setMessage("Maximum attempts reached. Please try again later.");
					//data.setUniqueKey(civilIdEntity.get().getId().toString());
					String uniqueKey = civilIdEntity.get().getId().toString();
					genericResult.setData(new InnerData(uniqueKey));
					return genericResult;
				} else {
					genericResult.setStatus("Failure");
					genericResult.setMessage("Invalid OTP. Attempts left: " + (otpMaxAttempts - otpEntity.getAttempts()));
					//data.setUniqueKey(civilIdEntity.get().getId().toString());
					String uniqueKey = civilIdEntity.get().getId().toString();
					genericResult.setData(new InnerData(uniqueKey));
					return genericResult;
				}
			}

			// Check expiration time (e.g., within the last 10 minutes)
			LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(10);
			if (Objects.nonNull(otpEntity.getLastAttemptTime())
					&& otpEntity.getLastAttemptTime().isBefore(expirationTime)) {
				genericResult.setStatus("Failure");
				genericResult.setMessage("OTP expired. Please request a new OTP.");
				//data.setUniqueKey(civilIdEntity.get().getId().toString());
				String uniqueKey = civilIdEntity.get().getId().toString();
				genericResult.setData(new InnerData(uniqueKey));
				return genericResult;
			}

			// OTP validation successful, reset attempts
			otpEntity.setAttempts(0);
			otpEntity.setLastAttemptTime(LocalDateTime.now());
			otpRepository.save(otpEntity);

			genericResult.setStatus("Success");
			genericResult.setMessage("Success");
			//data.setUniqueKey(civilIdEntity.get().getId().toString());
			String uniqueKey = civilIdEntity.get().getId().toString();
			genericResult.setData(new InnerData(uniqueKey));
			return genericResult;
		}
		genericResult.setStatus("Failure");
		genericResult.setMessage("Something went wrong");
		return genericResult;
	}

	@Override
	public ValidationResultDTO resendOtp(String uniqueKey) throws MaxResendAttemptsException {
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
	public GenericResponseDTO<SecuritySettings> saveSecuritySettings(SecuritySettingsDTO securitySettingsDTO) {
		GenericResponseDTO<SecuritySettings> genericResult = new GenericResponseDTO();
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
			System.out.println("securitySettings id >> " + securitySettings.getId());
			securitySettingsRepository.save(securitySettings);
			System.out.println("securitySettings id >> " + securitySettings.getId());
			genericResult.setStatus("Success");
			genericResult.setMessage("Security settings updated successfully");
			genericResult.setData(securitySettings);
		} else {
			genericResult.setStatus("Error");
			genericResult.setMessage("Security settings not updated");
		}
		return genericResult;
	}

	private void generateAndSaveOtp(OtpEntity otpEntity) {
//		String newOtp = CommonUtils.generateRandomOtp();
		otpEntity.setOtp("1234");
		otpEntity.setLastAttemptTime(LocalDateTime.now());
		otpRepository.save(otpEntity);
		// Send the OTP to the user (e.g., via SMS, email, etc.)
	}

}
