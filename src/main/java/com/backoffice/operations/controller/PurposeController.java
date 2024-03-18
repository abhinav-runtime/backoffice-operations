package com.backoffice.operations.controller;

import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.PurposeService;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/v1/purpose")
public class PurposeController {

	private final PurposeService purposeService;

	public PurposeController(PurposeService purposeService) {
		this.purposeService = purposeService;
	}

	@GetMapping()
	public ResponseEntity<GenericResponseDTO<Object>> getPurposeList(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		return ResponseEntity.ok(purposeService.getPurposeList());
	}

	@GetMapping("/network/ACH")
	public ResponseEntity<Object> networkACH(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		return new ResponseEntity<>(purposeService.getPurposeNetworkACH(), HttpStatus.OK);
	}

}
