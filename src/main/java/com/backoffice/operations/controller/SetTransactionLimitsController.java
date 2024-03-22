package com.backoffice.operations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.payloads.CardSettingDto;
import com.backoffice.operations.payloads.SetCardControlsDTO;
import com.backoffice.operations.payloads.TransactionLimitsDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.SetCardControlService;
import com.backoffice.operations.service.TransactionLimitService;

@RestController
@RequestMapping("/api/cards")
public class SetTransactionLimitsController {

	@Autowired
	private TransactionLimitService transactionLimitService;

	@Autowired
	private SetCardControlService setCardControlService;

	@PostMapping("/setTransactionLimits")
	public ResponseEntity<GenericResponseDTO<Object>> setTransactionLimits(
			@RequestBody TransactionLimitsDTO transactionLimitsDTO,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> validationResultDTO = transactionLimitService
				.setTransactionLimits(transactionLimitsDTO);
		return ResponseEntity.ok(validationResultDTO);
	}

	@PostMapping("/setControls")
	public ResponseEntity<GenericResponseDTO<Object>> setControls(@RequestBody SetCardControlsDTO setCardControlsDTO,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> validationResultDTO = setCardControlService.setControls(setCardControlsDTO,
				token.substring("Bearer ".length()));
		return ResponseEntity.ok(validationResultDTO);
	}

	@GetMapping("/getControls")
	public ResponseEntity<GenericResponseDTO<Object>> getCardControlsLists(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> validationResultDTO = setCardControlService
				.getCardControlsLists(token.substring("Bearer ".length()));
		return ResponseEntity.ok(validationResultDTO);
	}

	@GetMapping("/getTransactionLimits")
	public ResponseEntity<GenericResponseDTO<Object>> getTransactionLimitsLists(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> validationResultDTO = transactionLimitService
				.getAllTransactionLimits(token.substring("Bearer ".length()));
		return ResponseEntity.ok(validationResultDTO);
	}

	@GetMapping("/getTransactionLimits/{uniqueKey}")
	public ResponseEntity<GenericResponseDTO<Object>> getTransactionLimitsByCustId(
			@PathVariable(name = "uniqueKey") String uniqueKey,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> validationResultDTO = transactionLimitService
				.getTransactionLimitsByCustId(uniqueKey);
		return ResponseEntity.ok(validationResultDTO);
	}

	@GetMapping("/getCardSetting/{uniqueKey}")
	public ResponseEntity<Object> getCardSetting(@PathVariable(name = "uniqueKey") String uniqueKey,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> validationResultDTO = transactionLimitService.getCardSetting(uniqueKey);
		return ResponseEntity.ok(validationResultDTO);
	}

	@PostMapping("/setCardSetting/{uniqueKey}")
	public ResponseEntity<Object> setCardSetting(@PathVariable(name = "uniqueKey") String uniqueKey,
			@RequestBody CardSettingDto cardSettingDto) {
		GenericResponseDTO<Object> validationResultDTO = transactionLimitService.setCardSetting(cardSettingDto,
				uniqueKey);
		return ResponseEntity.ok(validationResultDTO);
	}
}
