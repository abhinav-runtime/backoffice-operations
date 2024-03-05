package com.backoffice.operations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.BoAccountService;

@RestController
@RequestMapping("/bo/v1/Customer-account")
public class BoAccountController {
	@Autowired
	private BoAccountService boAccountService;
	
	@GetMapping("/details/{custNo}")
	private ResponseEntity<Object> getAccountDetails(@PathVariable String custNo) {
		GenericResponseDTO<Object> responseDTO = boAccountService.getAccountDetails(custNo);
		if (responseDTO.getStatus().equals("Failure")) {
			return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}else {
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		}
	}
}
