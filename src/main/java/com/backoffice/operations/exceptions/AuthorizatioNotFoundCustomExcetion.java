package com.backoffice.operations.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.backoffice.operations.payloads.common.GenericResponseDTO;

import java.util.HashMap;

@RestControllerAdvice
public class AuthorizatioNotFoundCustomExcetion {

	@ExceptionHandler(MissingRequestHeaderException.class)
	public ResponseEntity<GenericResponseDTO<Object>> handleAuthorizationHeaderMissing(
			MissingRequestHeaderException ex) {
		if (ex.getHeaderName().equals("Authorization")) {
			GenericResponseDTO<Object> errorResponse = new GenericResponseDTO<>();
			errorResponse.setStatus("Failure");
			errorResponse.setData(new HashMap<>());
			errorResponse.setMessage("Required header 'Authorization' is not present.");
			return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
		} else {
			GenericResponseDTO<Object> errorResponse = new GenericResponseDTO<>();
			errorResponse.setStatus("Failure");
			errorResponse.setData(new HashMap<>());
			errorResponse.setMessage("Required header " + ex.getHeaderName() + " is not present.");
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
	}
}
