package com.backoffice.operations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.entity.User;
import com.backoffice.operations.payloads.JWTAuthResponse;
import com.backoffice.operations.payloads.LoginDto;
import com.backoffice.operations.payloads.RegisterDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.AuthService;
import com.backoffice.operations.service.BOLoginLogSevice;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/bo/v1/auth")
public class BOAuthController {
	
	@Autowired
	private BOLoginLogSevice loginLogService;
	
	private AuthService authService;

	public BOAuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping(value = { "/login", "/signin" })
	public ResponseEntity<JWTAuthResponse> login(HttpServletRequest request, @RequestBody LoginDto loginDto){
		
		
//		System.out.println(request.getHeader("User-Agent"));
		String token = authService.login(loginDto);
		
		loginLogService.saveLoginLog(token, request.getHeader("User-Agent"));
		
		JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
		jwtAuthResponse.setAccessToken(token);

		return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
	}
	
	@PostMapping(value = { "/register", "/signup" })
	public GenericResponseDTO<User> register(@RequestBody RegisterDto registerDto) {
		GenericResponseDTO<User> genericResult = authService.register(registerDto);
		return genericResult;
	}
}
