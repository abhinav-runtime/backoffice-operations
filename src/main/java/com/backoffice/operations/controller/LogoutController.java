package com.backoffice.operations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.payloads.LogoutDto;
import com.backoffice.operations.payloads.ValidationResultDTO;
import com.backoffice.operations.service.LogoutService;


@RestController
@RequestMapping("/api/v1/auth")
public class LogoutController {
	
	@Autowired
	private LogoutService logoutService;
	
	
	@PostMapping("/logout")
	public ResponseEntity<ValidationResultDTO> logoutDevice(@RequestBody LogoutDto logoutDto,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		ValidationResultDTO validationResultDTO = logoutService.logout(logoutDto, token.substring("Bearer ".length()));
		return ResponseEntity.ok(validationResultDTO);
	}
}