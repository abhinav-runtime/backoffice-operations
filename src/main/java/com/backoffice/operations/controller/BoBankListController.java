package com.backoffice.operations.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.entity.BankList;
import com.backoffice.operations.payloads.BankListDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.security.BOUserToken;
import com.backoffice.operations.service.BankListService;

@RestController
@RequestMapping("/bo/v1/bank-list")
public class BoBankListController {
	@Autowired
	private BankListService bankListService;
	@Autowired
	private BOUserToken boUserToken;

	@GetMapping("/get-all-banks")
	public ResponseEntity<Object> getAllBanks() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<Object>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			List<BankList> bankList = bankListService.getAllBanks();
			if (bankList.size() == 0) {
				response.setStatus("Failure");
				response.setMessage("No bank found.");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			} else {
				response.setStatus("Success");
				response.setMessage("Bank list found successfully.");
				response.setData(bankList);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		}
	}

	@GetMapping("/get-by-id/{id}")
	public ResponseEntity<Object> getBankById(@PathVariable(name = "id") String id) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			response = bankListService.getBankById(id);
			if (response.getStatus().equals("Failure")) {
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		}
	}

	@GetMapping("/get-by-bic-code/{bicCode}")
	public ResponseEntity<Object> getBankByBicCode(@PathVariable(name = "bicCode") String bicCode) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			response = bankListService.getBankByBicCode(bicCode);
			if (response.getStatus().equals("Failure")) {
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		}

	}

	@GetMapping("/get-by-bank-name/{bankName}")
	public ResponseEntity<Object> getBankByBankName(@PathVariable(name = "bankName") String bankName) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			response = bankListService.getBankByBankName(bankName);
			if (response.getStatus().equals("Failure")) {
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		}
	}

	@PostMapping("/save-bank")
	public ResponseEntity<Object> addBank(@RequestBody BankListDTO requestDto) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			response = bankListService.addBank(requestDto);
			if (response.getStatus().equals("Failure")) {
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		}
	}

	@PutMapping("/update-bank/{id}")
	public ResponseEntity<Object> updateBank(@PathVariable(name = "id") String id,
			@RequestBody BankListDTO requestDto) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			response = bankListService.updateBankById(id, requestDto);
			if (response.getStatus().equals("Failure")) {
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		}
	}

	@DeleteMapping("/delete-bank/{id}")
	public ResponseEntity<Object> deleteBank(@PathVariable(name = "id") String id) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} else {
			response = bankListService.deleteBankById(id);
			if (response.getStatus().equals("Failure")) {
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		}
	}
}
