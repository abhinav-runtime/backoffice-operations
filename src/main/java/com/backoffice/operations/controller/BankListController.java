package com.backoffice.operations.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;

import com.backoffice.operations.entity.BankList;
import com.backoffice.operations.payloads.BankListDTO;
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

	@GetMapping("/get-by-id/{id}")
	public GenericResponseDTO<Object> getBankById(@PathVariable(name = "id") String id,
			@RequestHeader(HttpHeaders.AUTHORIZATION)  String token) {
		return bankListService.getBankById(id);
	}

	@GetMapping("/get-by-bic-code/{bicCode}")
	public GenericResponseDTO<Object> getBankByBicCode(@PathVariable(name = "bicCode") String bicCode,
			@RequestHeader(HttpHeaders.AUTHORIZATION)  String token) {
		return bankListService.getBankByBicCode(bicCode);
	}

	@GetMapping("/get-by-bank-name/{bankName}")
	public GenericResponseDTO<Object> getBankByBankName(@PathVariable(name = "bankName") String bankName,
			@RequestHeader(HttpHeaders.AUTHORIZATION)  String token) {
		return bankListService.getBankByBankName(bankName);
	}

	@PostMapping("/save-bank")
	public GenericResponseDTO<Object> addBank(@RequestBody BankListDTO requestDto) {
		return bankListService.addBank(requestDto);
	}

	@PutMapping("/update-bank/{id}")
	public GenericResponseDTO<Object> updateBank(@PathVariable(name = "id") String id,
			@RequestBody BankListDTO requestDto) {
		return bankListService.updateBankById(id, requestDto);
	}

	@DeleteMapping("/delete-bank/{id}")
	public GenericResponseDTO<Object> deleteBank(@PathVariable(name = "id") String id) {
		return bankListService.deleteBankById(id);
	}
}
