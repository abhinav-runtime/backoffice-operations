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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.payloads.BiometricAttemptParameterDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.security.BOUserToken;
import com.backoffice.operations.service.BiometricAttemptService;

@RestController
@RequestMapping("/bo/v1/biometric-attempt-parameter")
public class BiometricAttemptParameterController {
	@Autowired
	private BiometricAttemptService biometricAttemptService;
	@Autowired
	private BOUserToken boUserToken;

	@PutMapping("/update")
	public ResponseEntity<Object> updateBiometricAttemptParameter(
			@RequestBody BiometricAttemptParameterDto requestDto) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			BiometricAttemptParameterDto data = biometricAttemptService.updateBiometricAttemptParameter(requestDto);
			if (data == null || data.equals(null)) {
				response.setMessage("Bimetric Attempt parameter not found");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			response.setData(data);
			response.setMessage("Bimetric Attempt parameter updated successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@GetMapping("/get")
	public ResponseEntity<Object> getBiometricAttemptParameter() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			BiometricAttemptParameterDto data = biometricAttemptService.getBiometricAttemptParameter();
			if (data == null || data.equals(null)) {
				response.setMessage("Bimetric Attempt parameter not found");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			response.setData(data);
			response.setMessage("Bimetric Attempt parameter fetched successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PostMapping("/insert")
	public ResponseEntity<Object> insertBiometricAttemptParameter(
			@RequestBody BiometricAttemptParameterDto requestDto) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			BiometricAttemptParameterDto data = biometricAttemptService.createBiometricAttemptParameter(requestDto);
			if (data == null || data.equals(null)) {
				response.setMessage("Bimetric Attempt parameter already exists");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			response.setData(data);
			response.setMessage("Bimetric Attempt parameter inserted successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Object> deleteBiometricAttemptParameter() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			BiometricAttemptParameterDto data = biometricAttemptService.deleteBiometricAttemptParameter();
			if (data == null || data.equals(null)) {
				response.setMessage("Bimetric Attempt parameter not found");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			response.setData(data);
			response.setMessage("Bimetric Attempt parameter deleted successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}
}

@RestController
@RequestMapping("/api/v1/biometric-attempt-parameter")
class BiometricAttemptParameterControllerAPK {
	@Autowired
	private BiometricAttemptService biometricAttemptService;

	@PutMapping("/update")
	public ResponseEntity<Object> updateBiometricAttemptParameter(
			@RequestBody BiometricAttemptParameterDto requestDto) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();

		BiometricAttemptParameterDto data = biometricAttemptService.updateBiometricAttemptParameter(requestDto);
		if (data == null || data.equals(null)) {
			response.setMessage("Bimetric Attempt parameter not found");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		response.setData(data);
		response.setMessage("Bimetric Attempt parameter updated successfully");
		response.setStatus("Success");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/get")
	public ResponseEntity<Object> getBiometricAttemptParameter() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();

		BiometricAttemptParameterDto data = biometricAttemptService.getBiometricAttemptParameter();
		if (data == null || data.equals(null)) {
			response.setMessage("Bimetric Attempt parameter not found");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		response.setData(data);
		response.setMessage("Bimetric Attempt parameter fetched successfully");
		response.setStatus("Success");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
