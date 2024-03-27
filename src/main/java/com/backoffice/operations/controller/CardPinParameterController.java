package com.backoffice.operations.controller;

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

import com.backoffice.operations.payloads.CardPinParameterDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.security.BOUserToken;
import com.backoffice.operations.service.CardPinParameterService;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/bo/v1/card-pin-parameter")
public class CardPinParameterController {
	@Autowired
	private CardPinParameterService cardPinParameterService;
	@Autowired
	private BOUserToken boUserToken;

	@PutMapping("/update")
	public ResponseEntity<Object> updateCardPinParameter(@RequestBody CardPinParameterDto requestDto) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			CardPinParameterDto data = cardPinParameterService.updateCardPinParameter(requestDto);
			if (data == null || data.equals(null)) {
				response.setMessage("CardPin parameter not found");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			response.setData(data);
			response.setMessage("CardPin parameter updated successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@GetMapping("/get")
	public ResponseEntity<Object> getCardPinParameter() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			CardPinParameterDto data = cardPinParameterService.getCardPinParameter();
			if (data == null || data.equals(null)) {
				response.setMessage("CardPin parameter not found");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			response.setData(data);
			response.setMessage("CardPin parameter fetched successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PostMapping("/insert")
	public ResponseEntity<Object> insertCardPinParameter(@RequestBody CardPinParameterDto requestDto) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			CardPinParameterDto data = cardPinParameterService.createCardPinParameter(requestDto);
			if (data == null || data.equals(null)) {
				response.setMessage("CardPin parameter already exists");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			response.setData(data);
			response.setMessage("CardPin parameter inserted successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Object> deleteCardPinParameter() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			CardPinParameterDto data = cardPinParameterService.deleteCardPinParameter();
			if (data == null || data.equals(null)) {
				response.setMessage("CardPin parameter not found");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			response.setData(data);
			response.setMessage("CardPin parameter deleted successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}
}

@RestController
@RequestMapping("/api/v1/card-pin-parameter")
class CardPinParameterControllerAPK {
	@Autowired
	private CardPinParameterService cardPinParameterService;

	@PutMapping("/update")
	public ResponseEntity<Object> updateCardPinParameter(@RequestBody CardPinParameterDto requestDto,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		CardPinParameterDto data = cardPinParameterService.updateCardPinParameter(requestDto);
		if (data == null || data.equals(null)) {
			response.setMessage("CardPin parameter not found");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		response.setData(data);
		response.setMessage("CardPin parameter updated successfully");
		response.setStatus("Success");
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("/get")
	public ResponseEntity<Object> getCardPinParameter(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();

		CardPinParameterDto data = cardPinParameterService.getCardPinParameter();
		if (data == null || data.equals(null)) {
			response.setMessage("CardPin parameter not found");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		response.setData(data);
		response.setMessage("CardPin parameter fetched successfully");
		response.setStatus("Success");
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
}
