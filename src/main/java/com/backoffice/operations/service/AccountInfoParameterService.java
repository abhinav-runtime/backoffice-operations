package com.backoffice.operations.service;

import com.backoffice.operations.payloads.AccountInfoParameterDto;

public interface AccountInfoParameterService {
	AccountInfoParameterDto getAccountInfoParameter();
	AccountInfoParameterDto updateAccountInfoParameter(AccountInfoParameterDto accountInfoParameterDto);
	AccountInfoParameterDto createAccountInfoParameter(AccountInfoParameterDto accountInfoParameterDto);
	AccountInfoParameterDto deleteAccountInfoParameter();
}
