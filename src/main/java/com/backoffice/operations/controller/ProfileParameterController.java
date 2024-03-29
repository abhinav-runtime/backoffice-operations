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

import com.backoffice.operations.payloads.ProfileParameterDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.security.BOUserToken;
import com.backoffice.operations.service.ProfileParameterService;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/bo/v1/profile-parameter")
public class ProfileParameterController {
	@Autowired
	private ProfileParameterService profileParameterService;
	@Autowired
	private BOUserToken boUserToken;

	@PutMapping("/update")
	public ResponseEntity<Object> updateProfileParameterParameter(@RequestBody ProfileParameterDto requestDto) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			ProfileParameterDto data = profileParameterService.updateProfileParameter(requestDto);
			if (data == null || data.equals(null)) {
				response.setMessage("Profile parameter not found");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			response.setData(data);
			response.setMessage("Profile parameter updated successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@GetMapping("/get")
	public ResponseEntity<Object> getProfileParameterParameter() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			ProfileParameterDto data = profileParameterService.getProfileParameter();
			if (data == null || data.equals(null)) {
				response.setMessage("Profile parameter not found");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			response.setData(data);
			response.setMessage("Profile parameter fetched successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PostMapping("/insert")
	public ResponseEntity<Object> insertProfileParameterParameter(@RequestBody ProfileParameterDto requestDto) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			ProfileParameterDto data = profileParameterService.createProfileParameter(requestDto);
			if (data == null || data.equals(null)) {
				response.setMessage("Profile parameter already exists");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			response.setData(data);
			response.setMessage("Profile parameter inserted successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Object> deleteProfileParameterParameter() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			ProfileParameterDto data = profileParameterService.deleteProfileParameter();
			if (data == null || data.equals(null)) {
				response.setMessage("Profile parameter not found");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			response.setData(data);
			response.setMessage("Profile parameter deleted successfully");
			response.setStatus("Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}
}

@RestController
@RequestMapping("/api/v1/profile-parameter")
class ProfileParameterControllerAPK {
	@Autowired
	private ProfileParameterService profileParameterService;

	@PutMapping("/update")
	public ResponseEntity<Object> updateProfileParameterParameter(@RequestBody ProfileParameterDto requestDto,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();

		ProfileParameterDto data = profileParameterService.updateProfileParameter(requestDto);
		if (data == null || data.equals(null)) {
			response.setMessage("Profile parameter not found");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		response.setData(data);
		response.setMessage("Profile parameter updated successfully");
		response.setStatus("Success");
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("/get")
	public ResponseEntity<Object> getProfileParameterParameter(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();

		ProfileParameterDto data = profileParameterService.getProfileParameter();
		if (data == null || data.equals(null)) {
			response.setMessage("Profile parameter not found");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		response.setData(data);
		response.setMessage("Profile parameter fetched successfully");
		response.setStatus("Success");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
