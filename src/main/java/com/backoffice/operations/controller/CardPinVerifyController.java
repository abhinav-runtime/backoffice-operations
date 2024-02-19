package com.backoffice.operations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.payloads.CardPinVerifyDTO;
import com.backoffice.operations.payloads.ValidationResultDTO;
import com.backoffice.operations.service.CardPinVerifyService;


@RestController
@RequestMapping("/api/cardPin")
public class CardPinVerifyController {
	
	@Autowired
	private CardPinVerifyService cardPinVerifyService;
	
	@PostMapping("/verifyCardPin")
	public ResponseEntity<ValidationResultDTO> verifyCardPin(@RequestBody @Validated CardPinVerifyDTO cardPinVerifyDTO,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		ValidationResultDTO validationResultDTO = cardPinVerifyService.verifyCardPin(cardPinVerifyDTO,
				token.substring("Bearer ".length()));
		return ResponseEntity.ok(validationResultDTO);
	}
}
