package com.backoffice.operations.controller;

import com.backoffice.operations.payloads.AlizzTransferDto;
import com.backoffice.operations.payloads.AlizzTransferRequestDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.AlizzTransferService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/allizTransfer")
public class AlizzTransferController {

	private final AlizzTransferService alizzTransferService;

	public AlizzTransferController(AlizzTransferService alizzTransferService) {
		this.alizzTransferService = alizzTransferService;
	}

	@PostMapping
	public ResponseEntity<GenericResponseDTO<Object>> transferToAlizzAccount(
			@RequestBody AlizzTransferRequestDto alizzTransferRequestDto) throws JsonProcessingException {
		return ResponseEntity.ok(alizzTransferService.transferToAlizzAccount(alizzTransferRequestDto));
	}

	@GetMapping("/calculateFee")
	public ResponseEntity<GenericResponseDTO<Object>> calculateFee(@RequestParam(name = "amount") String amount,
			@RequestParam(name = "requestType") String requestType, @RequestParam(name = "uniqueKey") String uniqueKey,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws JsonProcessingException {
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		Map<String, Object> data = new HashMap<>();
		data.put("uniqueKey", uniqueKey);
		data.put("fee", Objects.nonNull(alizzTransferService.calculateFee()) ? alizzTransferService.calculateFee() : 0);
		data.put("amount", amount);
		data.put("requestType", requestType);
		responseDTO.setStatus("Success");
		responseDTO.setMessage("Success");
		responseDTO.setData(data);
		return ResponseEntity.ok(responseDTO);
	}
}
