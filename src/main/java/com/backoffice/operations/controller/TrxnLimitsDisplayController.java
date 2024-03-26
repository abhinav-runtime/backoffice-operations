package com.backoffice.operations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.payloads.TexnLimitsDisplayDto;
import com.backoffice.operations.service.TrxnLimitsDisplayService;

@RestController
@RequestMapping("/api/v1/trns-limits-display")
public class TrxnLimitsDisplayController {
	@Autowired
	private TrxnLimitsDisplayService trxnLimitsDisplayService;

	@GetMapping
	public Object getTransferLimit(@RequestBody TexnLimitsDisplayDto.ResquestDTO requestDTO,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		return trxnLimitsDisplayService.getTransferLimit(requestDTO);
	}

}
