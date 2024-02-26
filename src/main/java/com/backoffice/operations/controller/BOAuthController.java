package com.backoffice.operations.controller;

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
		return new ResponseEntity<>(boAuthService.login(loginDto), HttpStatus.OK);
	}

	@PostMapping(value = { "/register", "/signup" })
	public GenericResponseDTO<Object> register(@RequestBody BORegisterDTO registeruser) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {

			if (boUserToken.getRolesFromToken().isEmpty()) {
				response.setMessage("Something went wrong.");
				response.setStatus("Failure");
				response.setData(null);
				return response;
			}
			if (accessHelper.isAccessible("ACCOUNT_TYPES", "PUBLISH")) {
				return boAuthService.register(registeruser);
			}
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(null);
		} catch (Exception e) {
			response.setMessage("Something went wrong");
			response.setStatus("Failure");
		}
		return response;
	}

	@PostMapping(value = { "/suspend" })
	public GenericResponseDTO<Object> suspendUser(@RequestBody BOSuspendUserDTO userDto) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();

		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Token not found or expired");
			response.setStatus("Failure");
			response.setData(null);
			return response;
		}
		if (accessHelper.isAccessible("AUTHORIZATION", "MODIFY")) {
			return boAuthService.suspendUser(userDto);			
		}
		response.setMessage("User not have permission to operate this action");
		response.setStatus("Failure");
		response.setData(null);
		return response;
	}
}
