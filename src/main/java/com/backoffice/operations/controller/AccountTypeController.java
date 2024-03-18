package com.backoffice.operations.controller;

import com.backoffice.operations.payloads.AccountTypeDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.AccountTypeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accountType")
public class AccountTypeController {

    private final AccountTypeService accountTypeService;

    public AccountTypeController(AccountTypeService accountTypeService) {
        this.accountTypeService = accountTypeService;
    }

    @PostMapping
    public GenericResponseDTO<Object> createAccountType(@RequestBody List<AccountTypeDto> accountType) {
    	GenericResponseDTO<Object> response = new GenericResponseDTO<>();
    	response.setStatus("Success");
    	response.setMessage("Request created successfully");
    	response.setData(accountTypeService.createAccountType(accountType));
        return response;
    }

    @GetMapping
    public GenericResponseDTO<Object> getAllAccountTypes(@RequestHeader(HttpHeaders.AUTHORIZATION)  String token) {
    	GenericResponseDTO<Object> response = new GenericResponseDTO<>();
    	response.setStatus("Success");
    	response.setMessage("Request retrieved successfully");
    	response.setData(accountTypeService.getAllAccountTypes());
        return response;
    }

    @GetMapping("/{productCode}")
    public GenericResponseDTO<Object> getAccountTypeById(@PathVariable String productCode, @RequestHeader(HttpHeaders.AUTHORIZATION)  String token) {
    	GenericResponseDTO<Object> response = new GenericResponseDTO<>();
    	response.setStatus("Success");
    	response.setMessage("Request retrieved successfully");
    	response.setData(accountTypeService.getAccountTypeById(productCode));
        return response;
    }

    @PutMapping("/{id}")
    public GenericResponseDTO<Object> updateAccountType(@PathVariable String id, @RequestBody AccountTypeDto accountTypeDto) {
    	GenericResponseDTO<Object> response = new GenericResponseDTO<>();
    	response.setStatus("Success");
    	response.setMessage("Request updated successfully");
    	response.setData(accountTypeService.updateAccountType(id, accountTypeDto));
        return response;
    }

    @DeleteMapping("/{id}")
    public GenericResponseDTO<Object> deleteAccountType(@PathVariable String id) {
    	GenericResponseDTO<Object> response = new GenericResponseDTO<>();
        accountTypeService.deleteAccountType(id);
        response.setStatus("Success");
        response.setMessage("Request deleted successfully");
        response.setData(new HashMap<>());
        return response;
    }
}

