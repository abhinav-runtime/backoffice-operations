package com.backoffice.operations.controller;

import org.springframework.http.HttpHeaders;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.payloads.CardNumberParameterDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.security.BOUserToken;
import com.backoffice.operations.service.CardNumberParameterService;

@RestController
@RequestMapping("/bo/v1/cardNumberParameter")
public class CardNumberParameterController {
	@Autowired
	private CardNumberParameterService cardNumberParameterService;
	@Autowired
	private BOUserToken boUserToken;

	@PutMapping("/update")
	public ResponseEntity<Object> updateCardNumberParameter(@RequestBody CardNumberParameterDTO requestDto) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			CardNumberParameterDTO data = cardNumberParameterService.updateCardNumberParameter(requestDto);
			if (data == null || data.equals(null)) {
				response.setMessage("Card number parameter not found");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			response.setData(data);
			response.setMessage("Card number parameter updated successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@GetMapping("/get")
	public ResponseEntity<Object> getCardNumberParameter() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			CardNumberParameterDTO data = cardNumberParameterService.getCardNumberParameter();
			if (data == null || data.equals(null)) {
				response.setMessage("Card number parameter not found");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			response.setData(data);
			response.setMessage("Card number parameter fetched successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PostMapping("/insert")
	public ResponseEntity<Object> insertCardNumberParameter(@RequestBody CardNumberParameterDTO requestDto) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			CardNumberParameterDTO data = cardNumberParameterService.createCardNumberParameter(requestDto);
			if (data == null || data.equals(null)) {
				response.setMessage("Card number parameter already exists");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			response.setData(data);
			response.setMessage("Card number parameter inserted successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Object> deleteCardNumberParameter() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			CardNumberParameterDTO data = cardNumberParameterService.deleteCardNumberParameter();
			if (data == null || data.equals(null)) {
				response.setMessage("Card number parameter not found");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			response.setData(data);
			response.setMessage("Card number parameter deleted successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}
}

@RestController
@RequestMapping("/api/v1/cardNumberParameter")
class CardNumberParameterControllerAPK {
	@Autowired
	private CardNumberParameterService cardNumberParameterService;

	@PutMapping("/update")
	public ResponseEntity<Object> updateCardNumberParameter(@RequestBody CardNumberParameterDTO requestDto,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();

		CardNumberParameterDTO data = cardNumberParameterService.updateCardNumberParameter(requestDto);
		if (data == null || data.equals(null)) {
			response.setMessage("Card number parameter not found");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		response.setData(data);
		response.setMessage("Card number parameter updated successfully");
		response.setStatus("Success");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/get")
	public ResponseEntity<Object> getCardNumberParameter(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();

		CardNumberParameterDTO data = cardNumberParameterService.getCardNumberParameter();
		if (data == null || data.equals(null)) {
			response.setMessage("Card number parameter not found");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		response.setData(data);
		response.setMessage("Card number parameter fetched successfully");
		response.setStatus("Success");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
