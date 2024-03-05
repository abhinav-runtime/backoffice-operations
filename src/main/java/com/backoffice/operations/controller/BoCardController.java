package com.backoffice.operations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.BoCarddetailService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/bo/v1/card")
public class BoCardController {
	@Autowired
	private BoCarddetailService boCardDetailService;
	
	@GetMapping("/details/{custNo}")
	public ResponseEntity<Object> fetchCardDetails(@PathVariable String custNo) {
		GenericResponseDTO<Object> responseDTO = boCardDetailService.fetchCardDeatils(custNo);
		if (responseDTO.getStatus().equals("Failure")) {
			return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}else {
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		}
	}
	
	@GetMapping("/preference/{custNo}")
	public ResponseEntity<Object> fetchPreference(@PathVariable String custNo) {
		GenericResponseDTO<Object> responseDTO = boCardDetailService.fetchPreference(custNo);
		if (responseDTO.getStatus().equals("Failure")) {
			return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		}
	}
	
	@PostMapping("/preference")
	public ResponseEntity<Object> setPreference(@RequestBody JsonNode requestBody) {
		GenericResponseDTO<Object> responseDTO = boCardDetailService.setPreference(requestBody);
		if (responseDTO.getStatus().equals("Failure")) {
			return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		}
	}
}
