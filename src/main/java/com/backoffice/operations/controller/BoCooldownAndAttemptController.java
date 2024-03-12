package com.backoffice.operations.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.payloads.CooldownAndAttemptDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.security.BOUserToken;
import com.backoffice.operations.service.BoCooldownAndAttemptUpdateService;

@RestController
@RequestMapping("/bo/cooldown-and-attempt-update")
public class BoCooldownAndAttemptController {
	@Autowired
	private BoCooldownAndAttemptUpdateService boCooldownAndAttemptUpdateService;
	@Autowired
	private BOUserToken boUserToken;

	@PutMapping("/otp-parameter")
	public ResponseEntity<Object> updateOtpParameter(@RequestBody CooldownAndAttemptDTO requestDto) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			CooldownAndAttemptDTO data = boCooldownAndAttemptUpdateService.updateOtpParameter(requestDto);
			response.setData(data);
			response.setMessage("Otp parameter updated successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PutMapping("/card-pin-parameter")
	public ResponseEntity<Object> updateCardPinParameter(@RequestBody CooldownAndAttemptDTO requestDto) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			CooldownAndAttemptDTO data = boCooldownAndAttemptUpdateService.updateCardPinParameter(requestDto);
			response.setData(data);
			response.setMessage("Card pin parameter updated successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PutMapping("/civil-id-parameter")
	public ResponseEntity<Object> updateCivilIdParameter(@RequestBody CooldownAndAttemptDTO requestDto) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			CooldownAndAttemptDTO data = boCooldownAndAttemptUpdateService.updateCivilIdParameter(requestDto);
			response.setData(data);
			response.setMessage("Civil id parameter updated successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}
}
