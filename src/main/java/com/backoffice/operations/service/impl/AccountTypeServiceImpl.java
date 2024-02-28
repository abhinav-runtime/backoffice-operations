package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.AccountType;
import com.backoffice.operations.exceptions.ResourceNotFoundException;
import com.backoffice.operations.payloads.AccountTypeDto;
import com.backoffice.operations.repository.AccountTypeRepository;
import com.backoffice.operations.service.AccountTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountTypeServiceImpl implements AccountTypeService {


    private final AccountTypeRepository accountTypeRepository;
    private final ObjectMapper objectMapper;

    public AccountTypeServiceImpl(AccountTypeRepository accountTypeRepository, ObjectMapper objectMapper) {
        this.accountTypeRepository = accountTypeRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<AccountTypeDto> createAccountType(List<AccountTypeDto> accountTypeDtos) {
        if (accountTypeDtos.isEmpty()) {
            return Collections.emptyList();
        }

        List<AccountType> accountTypes = accountTypeDtos.stream()
                .map(AccountTypeServiceImpl::getAccountType)
                .collect(Collectors.toList());

        List<AccountType> savedAccountTypes = accountTypeRepository.saveAll(accountTypes);

        return savedAccountTypes.stream()
                .map(AccountTypeServiceImpl::getAccountTypeDto) // Assuming fromEntity() converts Entity to DTO
                .collect(Collectors.toList());
    }


    @Override
    public List<AccountTypeDto> getAllAccountTypes() {
        List<AccountType> accountTypeList = accountTypeRepository.findAll();

        return accountTypeList.stream()
                .map(AccountTypeServiceImpl::getAccountTypeDto)
                .collect(Collectors.toList());
    }


    @Override
    public AccountTypeDto getAccountTypeById(String productCode) {
        return Optional.of(accountTypeRepository.findByCbsProductCode(productCode))
                .map(AccountTypeServiceImpl::getAccountTypeDto)
                .orElse(null);
    }

    private static AccountTypeDto getAccountTypeDto(AccountType accountType) {
        return AccountTypeDto.builder()
                .accountType(accountType.getAccountType())
                .description(accountType.getDescription())
                .cbsProductCode(accountType.getCbsProductCode())
                .editAccountInfo(accountType.getEditAccountInfo())
                .billPayments(accountType.getBillPayments())
                .requestDebitCard(accountType.getRequestDebitCard())
                .requestsChequeBook(accountType.getRequestsChequeBook())
                .visibility(accountType.getVisibility())
                .id(accountType.getId())
                .transfers(accountType.getTransfers())
                .build();
    }

    @Override
    public AccountTypeDto updateAccountType(String id, AccountTypeDto accountTypeDto) {

        if (accountTypeRepository.existsById(id)) {
            AccountType accountType = getAccountType(accountTypeDto);
            accountType.setId(id);
            accountType = accountTypeRepository.save(accountType);
            return getAccountTypeDto(accountType);
        } else {
            throw new ResourceNotFoundException("AccountType", "id", id);
        }
    }

    @Override
    public void deleteAccountType(String id) {
        accountTypeRepository.deleteById(id);
    }

    private static AccountType getAccountType(AccountTypeDto accountTypeDto) {
        return AccountType.builder()
                .accountType(Objects.nonNull(accountTypeDto.getAccountType()) ? accountTypeDto.getAccountType() : "")
                .description(Objects.nonNull(accountTypeDto.getDescription()) ? accountTypeDto.getDescription() : "")
                .cbsProductCode(Objects.nonNull(accountTypeDto.getCbsProductCode()) ? accountTypeDto.getCbsProductCode() : "")
                .editAccountInfo(Objects.nonNull(accountTypeDto.getEditAccountInfo()) ? accountTypeDto.getEditAccountInfo() : "")
                .billPayments(Objects.nonNull(accountTypeDto.getBillPayments()) ? accountTypeDto.getBillPayments() : "")
                .requestDebitCard(Objects.nonNull(accountTypeDto.getRequestDebitCard()) ? accountTypeDto.getRequestDebitCard() : "")
                .requestsChequeBook(Objects.nonNull(accountTypeDto.getRequestsChequeBook()) ? accountTypeDto.getRequestsChequeBook() : "")
                .visibility(Objects.nonNull(accountTypeDto.getVisibility()) ? accountTypeDto.getVisibility() : "")
                .id(Objects.nonNull(accountTypeDto.getId()) ? accountTypeDto.getId() : "")
                .transfers(Objects.nonNull(accountTypeDto.getTransfers()) ? accountTypeDto.getTransfers() : "")
                .build();
    }
}
