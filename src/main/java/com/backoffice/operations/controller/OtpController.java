package com.backoffice.operations.controller;

import java.util.HashMap;
import java.util.Map;

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
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.security.JwtTokenProvider;
import com.backoffice.operations.service.BOCustomerService;
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
	@Autowired
	private BOCustomerService customerService;

	@PostMapping("/validate")
	public ResponseEntity<GenericResponseDTO<Object>> validateOtp(@RequestBody OtpRequestDTO otpRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> validationResultDTO = new GenericResponseDTO<>();
		try {
			validationResultDTO = otpService.validateOtp(otpRequest, token.substring("Bearer ".length()));
			return ResponseEntity.ok(validationResultDTO);
		} catch (OtpValidationException e) {
			return ResponseEntity.ok(validationResultDTO);
		}
	}

	@PostMapping("/resend")
	public ResponseEntity<GenericResponseDTO<Object>> resendOtp(@RequestParam(name = "uniqueKey" ) String uniqueKey,
			@RequestParam(name = "lang") String lang,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> validationResultDTO = new GenericResponseDTO<>();
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

	@GetMapping("/civil/{civilId}/{lang}")
	public ResponseEntity<GenericResponseDTO<Object>> validateCivilId(@PathVariable @Validated String civilId,
			@PathVariable String lang,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> validationResultDTO = civilIdService.validateCivilId(civilId,
				token.substring("Bearer ".length()));
		return ResponseEntity.ok(validationResultDTO);
	}

	@PostMapping("/card/verifyCard")
	public ResponseEntity<GenericResponseDTO<Object>> verifyCard(@RequestBody @Validated EntityIdDTO entityIdDTO,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> validationResultDTO = civilIdService.verifyCard(entityIdDTO,
				token.substring("Bearer ".length()));
		return ResponseEntity.ok(validationResultDTO);
	}

	@PostMapping("/pin/storeAndSetPin")
	public ResponseEntity<GenericResponseDTO<Object>> storeAndSetPin(@RequestBody GetPinDTO pinRequestDTO,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> validationResultDTO = pinService.storeAndSetPin(pinRequestDTO, token.substring("Bearer ".length()));
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
	public ResponseEntity<GenericResponseDTO<Object>> updateSecuritySettings(@RequestBody SecuritySettingsDTO securitySettingsDTO) {
		otpService.saveSecuritySettings(securitySettingsDTO);
		customerService.addOnboardCustomer(securitySettingsDTO.getUniqueKey()); // It's indicate Onboarding completed
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		Map<String, Object> data = new HashMap<>();
		responseDTO.setStatus("Success");
		responseDTO.setMessage("Security settings updated successfully");
		data.put("uniqueKey",securitySettingsDTO.getUniqueKey());
		responseDTO.setData(data);
		return ResponseEntity.ok(responseDTO);
	}
	
	@PostMapping("/signIn")
	public ResponseEntity<GenericResponseDTO<Object>> signIn(@RequestBody LoginFlagDTO loginFlagDTO) {
		GenericResponseDTO<Object> validationResultDTO = loginHistoryService.saveLoginFlag(loginFlagDTO);
		return ResponseEntity.ok(validationResultDTO);
	}
}
