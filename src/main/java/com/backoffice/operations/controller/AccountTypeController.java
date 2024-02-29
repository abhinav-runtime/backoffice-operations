package com.backoffice.operations.controller;

import com.backoffice.operations.payloads.AccountTypeDto;
import com.backoffice.operations.service.AccountTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accountType")
public class AccountTypeController {

    private final AccountTypeService accountTypeService;

    public AccountTypeController(AccountTypeService accountTypeService) {
        this.accountTypeService = accountTypeService;
    }

    @PostMapping
    public List<AccountTypeDto> createAccountType(@RequestBody List<AccountTypeDto> accountType) {
        return accountTypeService.createAccountType(accountType);
    }

    @GetMapping
    public List<AccountTypeDto> getAllAccountTypes() {
        return accountTypeService.getAllAccountTypes();
    }

    @GetMapping("/{productCode}")
    public AccountTypeDto getAccountTypeById(@PathVariable String productCode) {
        return accountTypeService.getAccountTypeById(productCode);
    }

    @PutMapping("/{id}")
    public AccountTypeDto updateAccountType(@PathVariable String id, @RequestBody AccountTypeDto accountTypeDto) {
        return accountTypeService.updateAccountType(id, accountTypeDto);
    }

    @DeleteMapping("/{id}")
    public void deleteAccountType(@PathVariable String id) {
        accountTypeService.deleteAccountType(id);
    }
}

