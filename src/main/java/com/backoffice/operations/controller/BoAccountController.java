package com.backoffice.operations.controller;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.security.BOUserToken;
import com.backoffice.operations.service.BoAccountService;
import com.backoffice.operations.service.impl.BoAccessHelper;

@RestController
@RequestMapping("/bo/v1/customer-account")
public class BoAccountController {
	private static final Logger logger = LoggerFactory.getLogger(BoAccountController.class);
	@Autowired
	private BoAccountService boAccountService;
	@Autowired
	private BOUserToken boUserToken;
	@Autowired
	private BoAccessHelper accessHelper;

	@GetMapping("/details/{custNo}")
	private ResponseEntity<Object> getAccountDetails(@PathVariable(name = "custNo") String custNo) {
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		try {
			if (boUserToken.getRolesFromToken().isEmpty()) {
				responseDTO.setMessage("Something went wrong.");
				responseDTO.setStatus("Failure");
				responseDTO.setData(new HashMap<>());
				return new ResponseEntity<>(responseDTO, HttpStatus.UNAUTHORIZED);
			}
			if (accessHelper.isAccessible("CUSTOMERS_INDIVIDUAL", "VIEW")) {
				responseDTO = boAccountService.getAccountDetails(custNo);
				if (responseDTO.getStatus().equals("Failure")) {
					return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
				} else {
					return new ResponseEntity<>(responseDTO, HttpStatus.OK);
				}
			} else {
				responseDTO.setMessage("Something went wrong.");
				responseDTO.setStatus("Failure");
				responseDTO.setData(new HashMap<>());
				return new ResponseEntity<>(responseDTO, HttpStatus.METHOD_NOT_ALLOWED);
			}
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			responseDTO.setMessage("Something went wrong.");
			responseDTO.setStatus("Failure");
			responseDTO.setData(new HashMap<>());
			return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
