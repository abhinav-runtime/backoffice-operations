package com.backoffice.operations.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.payloads.BORegisterDTO;
import com.backoffice.operations.payloads.BOSuspendUserDTO;
import com.backoffice.operations.payloads.LoginDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.security.BOUserToken;
import com.backoffice.operations.service.BOAuthService;
import com.backoffice.operations.service.impl.BoAccessHelper;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/bo/v1/auth")
public class BOAuthController {

	@Autowired
	private BOAuthService boAuthService;
	@Autowired
	private BOUserToken boUserToken;
	@Autowired
	private BoAccessHelper accessHelper;

	@PostMapping(value = { "/login", "/signin" })
	public ResponseEntity<Object> login(HttpServletRequest request, @RequestBody LoginDto loginDto) {
		GenericResponseDTO<Object> response = boAuthService.login(loginDto);
		if (response.getStatus().equals("Failure")) {
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}else {
			return new ResponseEntity<>(response, HttpStatus.OK);			
		}
	}

	@PostMapping(value = { "/register", "/signup" })
	public ResponseEntity<Object> register(@RequestBody BORegisterDTO registeruser) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {

			if (boUserToken.getRolesFromToken().isEmpty()) {
				response.setMessage("Something went wrong.");
				response.setStatus("Failure");
				response.setData(null);
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
			}
			if (accessHelper.isAccessible("ACCOUNT_TYPES", "PUBLISH")) {
				response = boAuthService.register(registeruser);
				if (response.getStatus().equals("Failure")) {
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}
				return new ResponseEntity<>(response, HttpStatus.CREATED);
			}
			response.setMessage("User Not have permission.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
		} catch (Exception e) {
			response.setMessage("Something went wrong");
			response.setStatus("Failure");
		}
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping(value = { "/suspend" })
	public ResponseEntity<Object> suspendUser(@RequestBody BOSuspendUserDTO userDto) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();

		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		if (accessHelper.isAccessible("AUTHORIZATION", "MODIFY")) {
			response = boAuthService.suspendUser(userDto);
			if (response.getStatus().equals("Failure")) {
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

			} else {
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		}
		response.setMessage("User not have permission to operate this action");
		response.setStatus("Failure");
		response.setData(new HashMap<>());
		return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@PostMapping(value = { "/logout" })
	public ResponseEntity<Object> logout() {
		GenericResponseDTO<Object> response = boAuthService.logout();
		if (response.getStatus().equals("Failure")) {
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}
}
