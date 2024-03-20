package com.backoffice.operations.controller;

import com.backoffice.operations.payloads.AlizzTransferDto;
import com.backoffice.operations.payloads.AlizzTransferRequestDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.AlizzTransferService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/allizTransfer")
public class AlizzTransferController {
	private static final Logger logger = LoggerFactory.getLogger(AlizzTransferController.class);

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
			@RequestParam(name = "transferType") String transferType,
			@RequestParam(name = "uniqueKey") String uniqueKey,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		// TODO: based on transferType fetch codes from DB. transferType should have 3
		// enums: SELF/ALIZZ_TRANSFER/ACH_TRANSFER
		try {
			Map<String, Object> data = new HashMap<>();
			data.put("uniqueKey", uniqueKey);
			data.put("fee",
					Objects.nonNull(alizzTransferService.calculateFee(transferType))
							? alizzTransferService.calculateFee(transferType)
							: 0);
			data.put("amount", amount);
			data.put("transferType", transferType);
			responseDTO.setStatus("Success");
			responseDTO.setMessage("Success");
			responseDTO.setData(data);
			return ResponseEntity.ok(responseDTO);
		} catch (Exception e) {
			logger.error("Error while calculating fee : {}", e.getMessage());
			responseDTO.setStatus("Failure");
			responseDTO.setMessage("Something went wrong");
			return ResponseEntity.ok(responseDTO);
		}

	}
}
