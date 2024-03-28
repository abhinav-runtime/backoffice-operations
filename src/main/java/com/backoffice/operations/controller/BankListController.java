package com.backoffice.operations.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;

import com.backoffice.operations.entity.BankList;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.BankListService;

@RestController
@RequestMapping("/api/v1/bank-list")
public class BankListController {
	@Autowired
	private BankListService bankListService;

	@GetMapping("/get-all-banks")
	public GenericResponseDTO<Object> getAllBanks(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<Object>();
		List<BankList> bankList = bankListService.getAllBanks();
		if (bankList.size() == 0) {
			response.setStatus("Failure");
			response.setMessage("No bank found.");
			response.setData(new HashMap<>());
			return response;
		} else {
			response.setStatus("Success");
			response.setMessage("Bank list found successfully.");
			response.setData(bankList);
			return response;
		}
	}
}
