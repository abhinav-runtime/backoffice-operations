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

import com.backoffice.operations.payloads.TransactionLimitsDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.TransactionLimitService;

@RestController
@RequestMapping("/api/transaction")
public class SetTransactionLimitsController {
	
	@Autowired
	private TransactionLimitService transactionLimitService;

	@PostMapping("/setMerchantOutletLimits")
	public ResponseEntity<GenericResponseDTO<Object>> setMerchantOutletLimits(@RequestBody TransactionLimitsDTO transactionLimitsDTO, 
																			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> validationResultDTO = transactionLimitService.setMerchantOutletLimits(transactionLimitsDTO, token.substring("Bearer ".length()));
		return ResponseEntity.ok(validationResultDTO);
	}
	@PostMapping("/setOnlineShoppingLimits")
	public ResponseEntity<GenericResponseDTO<Object>> setOnlineShoppingLimits(@RequestBody TransactionLimitsDTO.OnlineShopping onlineShoppingLimitDTO,
																			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> validationResultDTO = transactionLimitService.setOnlineShoppingLimits(onlineShoppingLimitDTO, token.substring("Bearer ".length()));
		return ResponseEntity.ok(validationResultDTO);
	}
	@PostMapping("/setAtmWithdrawalLimits")
	public ResponseEntity<GenericResponseDTO<Object>> setAtmWithdrawalLimits(@RequestBody TransactionLimitsDTO.ATMwithdrawal atmWithdrawalLimitDTO,
																			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> validationResultDTO = transactionLimitService.setAtmWithdrawalLimits(atmWithdrawalLimitDTO, token.substring("Bearer ".length()));
		return ResponseEntity.ok(validationResultDTO);
	}
	@PostMapping("/setTapAndPayLimits")
	public ResponseEntity<GenericResponseDTO<Object>> setTapAndPayLimits(@RequestBody TransactionLimitsDTO.TapAndPay tapAndPayLimitDTO,
																		@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> validationResultDTO = transactionLimitService.setTapAndPayLimits(tapAndPayLimitDTO, token.substring("Bearer ".length()));
		return ResponseEntity.ok(validationResultDTO);
	}
	
	@GetMapping("/getTransactionLimits")
	public ResponseEntity<GenericResponseDTO<Object>> getTransactionLimitsLists(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {	
		GenericResponseDTO<Object> validationResultDTO = transactionLimitService.getAllTransactionLimits(token.substring("Bearer ".length()));
		return ResponseEntity.ok(validationResultDTO);
	}
	
	@GetMapping("/getTransactionLimits/{customerId}")
	public ResponseEntity<GenericResponseDTO<Object>> getTransactionLimitsByCustId(@PathVariable String customerId,
																				@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> validationResultDTO = transactionLimitService.getTransactionLimitsByCustId(customerId, token.substring("Bearer ".length()));
		return ResponseEntity.ok(validationResultDTO);
	}
}
