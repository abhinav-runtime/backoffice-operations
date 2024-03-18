package com.backoffice.operations.controller;

import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.ValidationService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/validate")
public class ValidationController {

	private final ValidationService validationService;

	public ValidationController(ValidationService validationService) {
		this.validationService = validationService;
	}

	@GetMapping("/user/{accountNumber}")
	public ResponseEntity<GenericResponseDTO<Object>> validateUserAccount(
			@PathVariable(name = "accountNumber") String accountNumber,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		return ResponseEntity.ok(validationService.validateUserAccount(accountNumber));
	}
}
