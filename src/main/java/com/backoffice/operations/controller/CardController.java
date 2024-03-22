package com.backoffice.operations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.payloads.CardStatusDto;
import com.backoffice.operations.service.CardService;

@RestController
@RequestMapping("/api/card")
public class CardController {

	@Autowired
	private CardService cardService;

	@GetMapping("/status")
	public ResponseEntity<Object> getCardStatus(@RequestParam(name = "uniqueKey") String uniqueKey,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		return ResponseEntity.ok(cardService.getCardStatus(uniqueKey));
	}

	@PostMapping("/block-unblock/{uniqueKey}")
	public ResponseEntity<Object> blockUnblockCard(@PathVariable(name = "uniqueKey") String uniqueKey,
			@RequestBody CardStatusDto.requestDto requestBody) {
		return ResponseEntity.ok(cardService.tempBlockAndUnblock(uniqueKey, requestBody));
	}
}
