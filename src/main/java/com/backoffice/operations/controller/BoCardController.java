package com.backoffice.operations.controller;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.payloads.BoTransactionsParemsDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.security.BOUserToken;
import com.backoffice.operations.service.BoCarddetailService;
import com.backoffice.operations.service.impl.BoAccessHelper;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.annotation.Nullable;

@RestController
@RequestMapping("/bo/v1/card")
public class BoCardController {
	private static final Logger logger = LoggerFactory.getLogger(BoCardController.class);
	@Autowired
	private BoCarddetailService boCardDetailService;
	@Autowired
	private BOUserToken boUserToken;
	@Autowired
	private BoAccessHelper accessHelper;

	@GetMapping("/details/{custNo}")
	public ResponseEntity<Object> fetchCardDetails(@PathVariable(value = "custNo") String custNo) {
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		try {
			if (boUserToken.getRolesFromToken().isEmpty()) {
				responseDTO.setMessage("Something went wrong.");
				responseDTO.setStatus("Failure");
				responseDTO.setData(new HashMap<>());
				return new ResponseEntity<>(responseDTO, HttpStatus.UNAUTHORIZED);
			}
			if (accessHelper.isAccessible("CUSTOMERS_INDIVIDUAL", "VIEW")) {
				responseDTO = boCardDetailService.fetchCardDeatils(custNo);
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

	@GetMapping("/get-preference/{custNo}")
	public ResponseEntity<Object> fetchPreference(@PathVariable(value = "custNo") String custNo) {
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		try {
			if (boUserToken.getRolesFromToken().isEmpty()) {
				responseDTO.setMessage("Something went wrong.");
				responseDTO.setStatus("Failure");
				responseDTO.setData(new HashMap<>());
				return new ResponseEntity<>(responseDTO, HttpStatus.UNAUTHORIZED);
			}
			if (accessHelper.isAccessible("CUSTOMERS_INDIVIDUAL", "VIEW")) {
				responseDTO = boCardDetailService.fetchPreference(custNo);
				if (responseDTO.getStatus().equals("Failure")) {
					return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
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

	@PostMapping("/set-preference")
	public ResponseEntity<Object> setPreference(@RequestBody JsonNode requestBody) {
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		try {
			if (boUserToken.getRolesFromToken().isEmpty()) {
				responseDTO.setMessage("Something went wrong.");
				responseDTO.setStatus("Failure");
				responseDTO.setData(new HashMap<>());
				return new ResponseEntity<>(responseDTO, HttpStatus.UNAUTHORIZED);
			}
			if (accessHelper.isAccessible("CUSTOMERS_INDIVIDUAL", "VIEW")) {
				responseDTO = boCardDetailService.setPreference(requestBody);
				if (responseDTO.getStatus().equals("Failure")) {
					return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
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

	@PostMapping("/transactions")
	public ResponseEntity<Object> getTransection(@RequestBody JsonNode preference,
			@Nullable @RequestParam(required = false) Long pageNo,
			@Nullable @RequestParam(required = false) Long pageSize,
			@Nullable @RequestParam(required = false) String fromDate,
			@Nullable @RequestParam(required = false) String toDate, @RequestParam String txnCategory) {
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		try {
			if (boUserToken.getRolesFromToken().isEmpty()) {
				responseDTO.setMessage("Something went wrong.");
				responseDTO.setStatus("Failure");
				responseDTO.setData(new HashMap<>());
				return new ResponseEntity<>(responseDTO, HttpStatus.UNAUTHORIZED);
			}
			if (accessHelper.isAccessible("CUSTOMERS_INDIVIDUAL", "VIEW")) {
				BoTransactionsParemsDto boTransactionsParemsDto = BoTransactionsParemsDto.builder()
						.pageNo(pageNo != null ? pageNo : 0).pageSize(pageSize != null ? pageSize : 20)
						.fromDate(fromDate).toDate(toDate).txnCategory(txnCategory).build();
				responseDTO = boCardDetailService.getTransections(boTransactionsParemsDto, preference);
				if (responseDTO.getStatus().equals("Failure")) {
					return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
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
