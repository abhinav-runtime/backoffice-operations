package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.*;
import com.backoffice.operations.exceptions.MaxResendAttemptsException;
import com.backoffice.operations.exceptions.OtpValidationException;
import com.backoffice.operations.payloads.*;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.*;
import com.backoffice.operations.security.JwtTokenProvider;
import com.backoffice.operations.service.OtpService;
import com.backoffice.operations.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class OtpServiceImpl implements OtpService {

	private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);
	@Autowired
	private OtpRepository otpRepository;

	@Autowired
	private SecuritySettingsRepository securitySettingsRepository;

	@Autowired
	private CivilIdRepository civilIdRepository;

	@Autowired
	private OtpParameterRepository otpParameterRepository;

	@Autowired
	@Qualifier("jwtAuth")
	private RestTemplate jwtAuthRestTemplate;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private UserRepository userRepository;

	@Value("${external.api.sms}")
	private String smsNotify;

	@Autowired
	private CommonUtils commonUtils;

	@Autowired
	private ProfileRepository profileRepository;

	@Value("${external.api.profile.update}")
	private String profileUpdate;

	@Override
	public GenericResponseDTO<Object> validateOtp(OtpRequestDTO otpRequest, String token)
			throws OtpValidationException {
//		long id = 1;
		String userEmail = jwtTokenProvider.getUsername(token);
		Optional<User> user = userRepository.findByEmail(userEmail);
		Map<String, String> data = new HashMap<>();
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		try {
			OtpParameter otpParameter = otpParameterRepository.findAll().get(0);
			int otpMaxAttempts = otpParameter.getOtpMaxAttempts();

			Optional<CivilIdEntity> civilIdEntity = civilIdRepository.findById(otpRequest.getUniqueKey());
			if (civilIdEntity.isPresent() && user.isPresent()) {
				OtpEntity otpEntity = otpRepository.findByUniqueKeyCivilId(otpRequest.getUniqueKey());
//				otpEntity.setOtp("123456");

				if (otpRequest.getOtp() == null) {
					data.put("uniqueKey", civilIdEntity.get().getId());
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
						responseDTO.setStatus("Failure");
						responseDTO.setMessage("Maximum attempts reached. Please try again later.");
						data.put("uniqueKey", civilIdEntity.get().getId());
						responseDTO.setData(data);
						return responseDTO;
					} else {
						responseDTO.setStatus("Failure");
						responseDTO.setMessage(
								"Invalid OTP. Attempts left: " + (otpMaxAttempts - otpEntity.getAttempts()));
						data.put("uniqueKey", civilIdEntity.get().getId());
						responseDTO.setData(data);
						return responseDTO;
					}
				}

				// Check expiration time (e.g., within the last 10 minutes)
				LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(otpParameter.getAttemptTimeOut());
				if (Objects.nonNull(otpEntity.getLastAttemptTime())
						&& otpEntity.getLastAttemptTime().isBefore(expirationTime)) {
					responseDTO.setStatus("Failure");
					responseDTO.setMessage("OTP expired. Please request a new OTP.");
					data.put("uniqueKey", civilIdEntity.get().getId());
					responseDTO.setData(data);
					return responseDTO;
				}

				// OTP validation successful, reset attempts
				otpEntity.setAttempts(0);
				otpEntity.setLastAttemptTime(LocalDateTime.now());
				otpRepository.save(otpEntity);
				responseDTO.setStatus("Success");
				responseDTO.setMessage("Success");
				data.put("uniqueKey", civilIdEntity.get().getId());
				responseDTO.setData(data);

				if (otpRequest.isProfileValidate()) {
					Optional<Profile> profile = profileRepository.findByUserId(user.get().getId());
					if (profile.isPresent()) {
						UpdateProfileRequestBank updateProfileRequestBank = new UpdateProfileRequestBank();
						updateProfileRequestBank.setEmailAddress(profile.get().getEmailId());
						updateProfileRequestBank.setMobileNumber(profile.get().getMobNum());

						ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
						if (Objects.nonNull(response.getBody())) {
							String apiUrl = profileUpdate + profile.get().getNId();
							HttpHeaders headers = new HttpHeaders();
							headers.setContentType(MediaType.APPLICATION_JSON);
							headers.setBearerAuth(response.getBody().getAccessToken());
							HttpEntity<UpdateProfileRequestBank> entity = new HttpEntity<>(updateProfileRequestBank,
									headers);

							ResponseEntity<Object> responseEntity = jwtAuthRestTemplate.exchange(apiUrl, HttpMethod.PUT,
									entity, Object.class);
							if (Objects.nonNull(responseEntity.getBody())) {
								user.get().setMobileNumber(updateProfileRequestBank.getMobileNumber());
								userRepository.save(user.get());
								responseDTO.setStatus("Success");
								responseDTO.setMessage("Success");
								data.put("uniqueKey", otpRequest.getUniqueKey());
								responseDTO.setData(data);
								return responseDTO;
							}
						}
					}
				}
				return responseDTO;
			}
		} catch (Exception e) {
			logger.error("Error: {}", e.getMessage());
			data.put("uniqueKey", otpRequest.getUniqueKey());
			responseDTO.setStatus("Failure");
			responseDTO.setMessage("Server Error");
			responseDTO.setData(data);
			return responseDTO;
		}
		data.put("uniqueKey", otpRequest.getUniqueKey());
		responseDTO.setStatus("Failure");
		responseDTO.setMessage("Server Error");
		responseDTO.setData(data);
		return responseDTO;
	}

	@Override
	public GenericResponseDTO<Object> transferOTP(String uniqueKey, String otp)
			throws OtpValidationException {

		OtpParameter otpParameter = otpParameterRepository.findAll().get(0);
		int otpMaxAttempts = otpParameter.getOtpMaxAttempts();
		OtpEntity otpEntity = otpRepository.findByUniqueKeyCivilId(uniqueKey);
        Optional<CivilIdEntity> civilIdEntity = civilIdRepository.findById(uniqueKey);
        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
        if (civilIdEntity.isPresent()) {
			if (otp == null) {
				Map<String, String> data = new HashMap<>();
				data.put("uniqueKey", civilIdEntity.get().getId());
				responseDTO.setStatus("Failure");
				responseDTO.setMessage("Invalid OTP or OTP expired");
				responseDTO.setData(data);
				return responseDTO;
			}

			if (!otpEntity.getOtp().equals(otp)) {
				otpEntity.setAttempts(otpEntity.getAttempts() + 1);
				otpEntity.setLastAttemptTime(LocalDateTime.now());
				otpRepository.save(otpEntity);

				if (otpEntity.getAttempts() >= otpMaxAttempts) {
					Map<String, String> data = new HashMap<>();
					responseDTO.setStatus("Failure");
					responseDTO.setMessage("Maximum attempts reached. Please try again later.");
					data.put("uniqueKey", civilIdEntity.get().getId());
					responseDTO.setData(data);
					return responseDTO;
				} else {
					Map<String, String> data = new HashMap<>();
					responseDTO.setStatus("Failure");
					responseDTO.setMessage("Invalid OTP. Attempts left: " + (otpMaxAttempts - otpEntity.getAttempts()));
					data.put("uniqueKey", civilIdEntity.get().getId());
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
				data.put("uniqueKey", civilIdEntity.get().getId());
				responseDTO.setData(data);
				return responseDTO;
			}

			// OTP validation successful, reset attempts
			otpEntity.setAttempts(0);
			otpEntity.setLastAttemptTime(LocalDateTime.now());
			otpEntity.setTransferWithinAlizzValidate(Boolean.TRUE);
			otpRepository.save(otpEntity);
			Map<String, String> data = new HashMap<>();
			responseDTO.setStatus("Success");
			responseDTO.setMessage("Success");
			data.put("uniqueKey", civilIdEntity.get().getId());
			responseDTO.setData(data);

			return responseDTO;
		}
		return responseDTO;
	}

	@Override
	public GenericResponseDTO<Object> resendOtp(String uniqueKey) throws MaxResendAttemptsException {
//		long id = 1;
		OtpParameter otpParameter = otpParameterRepository.findAll().get(0);
		int cooldownPeriod = otpParameter.getOtpResentTime();
		OtpEntity otpEntity = otpRepository.findByUniqueKeyCivilId(uniqueKey);
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		// TODO: return how many attempts
		if (otpEntity == null) {
			// First-time request, generate and save a new OTP
			otpEntity = new OtpEntity();
			otpEntity.setUniqueKeyCivilId(uniqueKey);
			generateAndSaveOtp(otpEntity);
		} else {
			// Check cooldown period
			LocalDateTime cooldownEndTime = otpEntity.getLastAttemptTime().plusSeconds(cooldownPeriod);
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
        otpEntity.setOtp("123456");
        otpEntity.setLastAttemptTime(LocalDateTime.now());
        otpRepository.save(otpEntity);
        // Send the OTP to the user (e.g., via SMS, email, etc.)
    }

}
