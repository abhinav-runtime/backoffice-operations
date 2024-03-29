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

import com.backoffice.operations.payloads.OtpParameterDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.security.BOUserToken;
import com.backoffice.operations.service.OtpParameterService;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/bo/v1/otp-parameter")
public class OtpParameterController {
	@Autowired
	private OtpParameterService otpParameterService;
	@Autowired
	private BOUserToken boUserToken;

	@PutMapping("/update")
	public ResponseEntity<Object> updateOtpParameter(@RequestBody OtpParameterDTO requestDto) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			OtpParameterDTO data = otpParameterService.updateOtpParameter(requestDto);
			if (data == null || data.equals(null)) {
				response.setMessage("Otp parameter not found");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			response.setData(data);
			response.setMessage("Otp parameter updated successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@GetMapping("/get")
	public ResponseEntity<Object> getOtpParameter() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			OtpParameterDTO data = otpParameterService.getOtpParameter();
			if (data == null || data.equals(null)) {
				response.setMessage("Otp parameter not found");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			response.setData(data);
			response.setMessage("Otp parameter fetched successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PostMapping("/insert")
	public ResponseEntity<Object> insertOtpParameter(@RequestBody OtpParameterDTO requestDto) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			OtpParameterDTO data = otpParameterService.createOtpParameter(requestDto);
			if (data == null || data.equals(null)) {
				response.setMessage("Otp parameter already exists");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			response.setData(data);
			response.setMessage("Otp parameter inserted successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Object> deleteOtpParameter() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			OtpParameterDTO data = otpParameterService.deleteOtpParameter();
			if (data == null || data.equals(null)) {
				response.setMessage("Otp parameter not found");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			response.setData(data);
			response.setMessage("Otp parameter deleted successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}
}

@RestController
@RequestMapping("/api/v1/otp-parameter")
class OtpParameterControllerAPK {
	@Autowired
	private OtpParameterService otpParameterService;

	@PutMapping("/update")
	public ResponseEntity<Object> updateOtpParameter(@RequestBody OtpParameterDTO requestDto,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();

		OtpParameterDTO data = otpParameterService.updateOtpParameter(requestDto);
		if (data == null || data.equals(null)) {
			response.setMessage("Otp parameter not found");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		response.setData(data);
		response.setMessage("Otp parameter updated successfully");
		response.setStatus("Success");
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("/get")
	public ResponseEntity<Object> getOtpParameter(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();

		OtpParameterDTO data = otpParameterService.getOtpParameter();
		if (data == null || data.equals(null)) {
			response.setMessage("Otp parameter not found");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		response.setData(data);
		response.setMessage("Otp parameter fetched successfully");
		response.setStatus("Success");
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
}
