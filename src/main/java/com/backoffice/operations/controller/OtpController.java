package com.backoffice.operations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import com.backoffice.operations.exceptions.MaxResendAttemptsException;
import com.backoffice.operations.exceptions.OtpValidationException;
import com.backoffice.operations.payloads.BlockUnblockActionDTO;
import com.backoffice.operations.payloads.EntityIdDTO;
import com.backoffice.operations.payloads.GetPinDTO;
import com.backoffice.operations.payloads.LoginFlagDTO;
import com.backoffice.operations.payloads.OtpRequestDTO;
import com.backoffice.operations.payloads.SecuritySettingsDTO;
import com.backoffice.operations.payloads.ValidationResultDTO;
import com.backoffice.operations.security.JwtTokenProvider;
import com.backoffice.operations.service.CardPinVerifyService;
import com.backoffice.operations.service.CivilIdService;
import com.backoffice.operations.service.LoginHistoryService;
import com.backoffice.operations.service.OtpService;
import com.backoffice.operations.service.PinService;

@RestController
@RequestMapping("/api/otp")
public class OtpController {
	

	@Autowired
	private OtpService otpService;
	@Autowired
	private CivilIdService civilIdService;
	@Autowired
	private PinService pinService;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private LoginHistoryService loginHistoryService;

	@PostMapping("/validate")
	public ResponseEntity<ValidationResultDTO> validateOtp(@RequestBody OtpRequestDTO otpRequest) {
		ValidationResultDTO validationResultDTO = new ValidationResultDTO();
		try {
			validationResultDTO = otpService.validateOtp(otpRequest);
			return ResponseEntity.ok(validationResultDTO);
		} catch (OtpValidationException e) {
			return ResponseEntity.ok(validationResultDTO);
		}
	}

	@PostMapping("/resend")
	public ResponseEntity<ValidationResultDTO> resendOtp(@RequestParam String uniqueKey,
			@RequestParam String lang,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		ValidationResultDTO validationResultDTO = new ValidationResultDTO();
		try {
			if (jwtTokenProvider.validateToken(token.substring("Bearer ".length()))) {
				validationResultDTO = otpService.resendOtp(uniqueKey);
				return ResponseEntity.ok(validationResultDTO);
			}
			return ResponseEntity.ok(validationResultDTO);
		} catch (MaxResendAttemptsException e) {
			return ResponseEntity.ok(validationResultDTO);
		}
	}

	@GetMapping("/civil/{civilId}/{uniqueId}/{lang}")
	public ResponseEntity<ValidationResultDTO> validateCivilId(@PathVariable @Validated String civilId,
															   @PathVariable String uniqueId,
															   @PathVariable String lang,
															   @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		ValidationResultDTO validationResultDTO = civilIdService.validateCivilId(civilId,
				token.substring("Bearer ".length()), uniqueId);
		return ResponseEntity.ok(validationResultDTO);
	}

	@PostMapping("/card/verifyCard")
	public ResponseEntity<ValidationResultDTO> verifyCard(@RequestBody @Validated EntityIdDTO entityIdDTO,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		ValidationResultDTO validationResultDTO = civilIdService.verifyCard(entityIdDTO,
				token.substring("Bearer ".length()));
		return ResponseEntity.ok(validationResultDTO);
	}

	@PostMapping("/pin/storeAndSetPin")
	public ResponseEntity<ValidationResultDTO> storeAndSetPin(@RequestBody GetPinDTO pinRequestDTO,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		ValidationResultDTO validationResultDTO = pinService.storeAndSetPin(pinRequestDTO, token.substring("Bearer ".length()));
		return ResponseEntity.ok(validationResultDTO);
	}

	@PostMapping("/card/fetchAllCustomerData")
	public ResponseEntity<Object> fetchAllCustomerData(@RequestBody @Validated EntityIdDTO entityIdDTO,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		Object responseDTO = civilIdService.fetchAllCustomerData(entityIdDTO, token.substring("Bearer ".length()));
		return ResponseEntity.ok(responseDTO);
	}

	@PostMapping("/card/blockUnblockCard")
	public ResponseEntity<Object> blockUnblockCard(@RequestBody @Validated BlockUnblockActionDTO blockUnblockCard) {
		Object responseDTO = civilIdService.blockUnblockCard(blockUnblockCard);
		return ResponseEntity.ok(responseDTO);
	}

	@PostMapping("/saveSettings")
	public ResponseEntity<ValidationResultDTO> updateSecuritySettings(@RequestBody SecuritySettingsDTO securitySettingsDTO) {
		otpService.saveSecuritySettings(securitySettingsDTO);
		ValidationResultDTO validationResultDTO = new ValidationResultDTO();
		ValidationResultDTO.Data data = new ValidationResultDTO.Data();
		validationResultDTO.setStatus("Success");
		validationResultDTO.setMessage("Security settings updated successfully");
		data.setUniqueKey(securitySettingsDTO.getUniqueKey());
		validationResultDTO.setData(data);
		return ResponseEntity.ok(validationResultDTO);
	}
	
	@PostMapping("/signIn")
	public ResponseEntity<ValidationResultDTO> signIn(@RequestBody LoginFlagDTO loginFlagDTO) {
		ValidationResultDTO validationResultDTO = loginHistoryService.saveLoginFlag(loginFlagDTO);
		return ResponseEntity.ok(validationResultDTO);
	}
}
