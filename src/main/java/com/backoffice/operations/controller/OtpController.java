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
import com.backoffice.operations.payloads.CardListResponse;
import com.backoffice.operations.payloads.EntityIdDTO;
import com.backoffice.operations.payloads.GetPinDTO;
import com.backoffice.operations.payloads.OtpRequestDTO;
import com.backoffice.operations.payloads.SecuritySettingsDTO;
import com.backoffice.operations.payloads.ValidationResultDTO;
import com.backoffice.operations.security.JwtTokenProvider;
import com.backoffice.operations.service.CivilIdService;
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

	@PostMapping("/validate")
	public ResponseEntity<String> validateOtp(@RequestBody OtpRequestDTO otpRequest) {
		try {
			otpService.validateOtp(otpRequest);
			return ResponseEntity.ok("OTP validation successful");
		} catch (OtpValidationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PostMapping("/resend")
	public ResponseEntity<String> resendOtp(@RequestParam String uniqueKey,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		try {
			if (jwtTokenProvider.validateToken(token.substring("Bearer ".length()))) {
				otpService.resendOtp(uniqueKey);
				return ResponseEntity.ok("OTP resent successfully");
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_GATEWAY.name());
		} catch (MaxResendAttemptsException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping("/civil/{civilId}")
	public ResponseEntity<ValidationResultDTO> validateCivilId(@PathVariable @Validated String civilId,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		ValidationResultDTO validationResultDTO = civilIdService.validateCivilId(civilId,
				token.substring("Bearer ".length()));
		return ResponseEntity.ok(validationResultDTO);
	}

	@PostMapping("/card/verifyCard")
	public ResponseEntity<CardListResponse> verifyCard(@RequestBody @Validated EntityIdDTO entityIdDTO,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		CardListResponse cardListResponse = civilIdService.verifyCard(entityIdDTO,
				token.substring("Bearer ".length()));
		return ResponseEntity.ok(cardListResponse);
	}

	@PostMapping("/pin/storeAndSetPin")
	public ResponseEntity<String> storeAndSetPin(@RequestBody GetPinDTO pinRequestDTO,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		if (pinService.storeAndSetPin(pinRequestDTO, token.substring("Bearer ".length()))) {
			return ResponseEntity.ok("Pin stored and set successfully");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error storing or setting pin");
		}
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
	public ResponseEntity<String> updateSecuritySettings(@RequestBody SecuritySettingsDTO securitySettingsDTO) {
		otpService.saveSecuritySettings(securitySettingsDTO);
		return new ResponseEntity<>("Security settings updated successfully", HttpStatus.OK);
	}

}
