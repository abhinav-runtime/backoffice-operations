package com.backoffice.operations.service;

import com.backoffice.operations.payloads.AccountTypeDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountTypeService {
    List<AccountTypeDto> createAccountType(List<AccountTypeDto> accountType);

    List<AccountTypeDto> getAllAccountTypes();

    AccountTypeDto getAccountTypeById(String productCode);

    AccountTypeDto updateAccountType(String id, AccountTypeDto accountType);

    void deleteAccountType(String id);
}


