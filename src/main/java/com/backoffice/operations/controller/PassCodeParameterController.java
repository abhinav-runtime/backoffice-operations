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

import com.backoffice.operations.payloads.PassCodeParameterDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.security.BOUserToken;
import com.backoffice.operations.service.PassCodeParameterService;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/bo/v1/passcode-parameter")
public class PassCodeParameterController {
	@Autowired
	private PassCodeParameterService parameterService;
	@Autowired
	private BOUserToken boUserToken;

	@PutMapping("/update")
	public ResponseEntity<Object> updatePassCodeParameter(@RequestBody PassCodeParameterDTO requestDto) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			PassCodeParameterDTO data = parameterService.updatePasscodeParameter(requestDto);
			if (data == null || data.equals(null)) {
				response.setMessage("Pass Code parameter not found");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			response.setData(data);
			response.setMessage("Pass Code parameter updated successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@GetMapping("/get")
	public ResponseEntity<Object> getPassCodeParameter() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			PassCodeParameterDTO data = parameterService.getPasscodeParameter();
			if (data == null || data.equals(null)) {
				response.setMessage("Pass Code parameter not found");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			response.setData(data);
			response.setMessage("Pass Code parameter fetched successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PostMapping("/insert")
	public ResponseEntity<Object> insertPassCodeParameter(@RequestBody PassCodeParameterDTO requestDto) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			PassCodeParameterDTO data = parameterService.createPassCodeParameter(requestDto);
			if (data == null || data.equals(null)) {
				response.setMessage("Pass Code parameter already exists");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			response.setData(data);
			response.setMessage("Pass Code parameter inserted successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Object> deletePassCodeParameter() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			PassCodeParameterDTO data = parameterService.deletePassCodeParameter();
			if (data == null || data.equals(null)) {
				response.setMessage("Pass Code parameter not found");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			response.setData(data);
			response.setMessage("Pass Code parameter deleted successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}
}

@RestController
@RequestMapping("/api/v1/passcode-parameter")
class PassCodeParameterControllerAPK {
	@Autowired
	private PassCodeParameterService parameterService;

	@PutMapping("/update")
	public ResponseEntity<Object> updatePassCodeParameter(@RequestBody PassCodeParameterDTO requestDto,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();

		PassCodeParameterDTO data = parameterService.updatePasscodeParameter(requestDto);
		if (data == null || data.equals(null)) {
			response.setMessage("Pass Code parameter not found");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		response.setData(data);
		response.setMessage("Pass Code parameter updated successfully");
		response.setStatus("Success");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/get")
	public ResponseEntity<Object> getPassCodeParameter(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();

		PassCodeParameterDTO data = parameterService.getPasscodeParameter();
		if (data == null || data.equals(null)) {
			response.setMessage("Pass Code parameter not found");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		response.setData(data);
		response.setMessage("Pass Code parameter fetched successfully");
		response.setStatus("Success");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
