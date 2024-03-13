package com.backoffice.operations.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.AccountInfoParameter;
import com.backoffice.operations.payloads.AccountInfoParameterDto;
import com.backoffice.operations.repository.AccountInfoParameterRepo;
import com.backoffice.operations.service.AccountInfoParameterService;

@Service
public class AccountInfoParameterServiceImp implements AccountInfoParameterService {
	private static final Logger logger = LoggerFactory.getLogger(AccountInfoParameterServiceImp.class);
	@Autowired
	private AccountInfoParameterRepo accountInfoParameterRepo;

	@Override
	public AccountInfoParameterDto getAccountInfoParameter() {
		try {
			AccountInfoParameter accountInfoParameter = accountInfoParameterRepo.findAll().get(0);
			return AccountInfoParameterDto.builder().nickNameMinLength(accountInfoParameter.getNickNameMinLength())
					.nickNameMaxLength(accountInfoParameter.getNickNameMaxLength())
					.noOfTimesUserCanChangeNickName(accountInfoParameter.getNoOfTimesUserCanChangeNickName())
					.makeAccountVisibleAttempts(accountInfoParameter.getMakeAccountVisibleAttempts())
					.alertOnTransactions(accountInfoParameter.getAlertOnTransactions())
					.alertOnLowBalanceAttempts(accountInfoParameter.getAlertOnLowBalanceAttempts()).build();
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}
	}

	@Override
	public AccountInfoParameterDto updateAccountInfoParameter(AccountInfoParameterDto accountInfoParameterDto) {
		try {
			AccountInfoParameter accountInfoParameter = accountInfoParameterRepo.findAll().get(0);
			if (accountInfoParameterDto.getNickNameMinLength() != 0) {
				accountInfoParameter.setNickNameMinLength(accountInfoParameterDto.getNickNameMinLength());
			}
			if (accountInfoParameterDto.getNickNameMaxLength() != 0) {
				accountInfoParameter.setNickNameMaxLength(accountInfoParameterDto.getNickNameMaxLength());
			}
			if (accountInfoParameterDto.getNoOfTimesUserCanChangeNickName() != 0) {
				accountInfoParameter
						.setNoOfTimesUserCanChangeNickName(accountInfoParameterDto.getNoOfTimesUserCanChangeNickName());
			}
			if (accountInfoParameterDto.getMakeAccountVisibleAttempts() != 0) {
				accountInfoParameter
						.setMakeAccountVisibleAttempts(accountInfoParameterDto.getMakeAccountVisibleAttempts());
			}
			if (accountInfoParameterDto.getAlertOnTransactions() != 0) {
				accountInfoParameter.setAlertOnTransactions(accountInfoParameterDto.getAlertOnTransactions());
			}
			if (accountInfoParameterDto.getAlertOnLowBalanceAttempts() != 0) {
				accountInfoParameter
						.setAlertOnLowBalanceAttempts(accountInfoParameterDto.getAlertOnLowBalanceAttempts());
			}
			accountInfoParameter = accountInfoParameterRepo.save(accountInfoParameter);
			return AccountInfoParameterDto.builder().nickNameMinLength(accountInfoParameter.getNickNameMinLength())
					.nickNameMaxLength(accountInfoParameter.getNickNameMaxLength())
					.noOfTimesUserCanChangeNickName(accountInfoParameter.getNoOfTimesUserCanChangeNickName())
					.makeAccountVisibleAttempts(accountInfoParameter.getMakeAccountVisibleAttempts())
					.alertOnTransactions(accountInfoParameter.getAlertOnTransactions())
					.alertOnLowBalanceAttempts(accountInfoParameter.getAlertOnLowBalanceAttempts()).build();
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}
	}

	@Override
	public AccountInfoParameterDto createAccountInfoParameter(AccountInfoParameterDto accountInfoParameterDto) {
		try {
			List<AccountInfoParameter> accountInfoParameterList = accountInfoParameterRepo.findAll();
			if (accountInfoParameterList.size() != 0) {
				return null;
			} else {
				AccountInfoParameter accountInfoParameter = AccountInfoParameter.builder()
						.nickNameMinLength(accountInfoParameterDto.getNickNameMinLength())
						.nickNameMaxLength(accountInfoParameterDto.getNickNameMaxLength())
						.noOfTimesUserCanChangeNickName(accountInfoParameterDto.getNoOfTimesUserCanChangeNickName())
						.makeAccountVisibleAttempts(accountInfoParameterDto.getMakeAccountVisibleAttempts())
						.alertOnTransactions(accountInfoParameterDto.getAlertOnTransactions())
						.alertOnLowBalanceAttempts(accountInfoParameterDto.getAlertOnLowBalanceAttempts()).build();
				accountInfoParameter = accountInfoParameterRepo.save(accountInfoParameter);
				return AccountInfoParameterDto.builder().nickNameMinLength(accountInfoParameter.getNickNameMinLength())
						.nickNameMaxLength(accountInfoParameter.getNickNameMaxLength())
						.noOfTimesUserCanChangeNickName(accountInfoParameter.getNoOfTimesUserCanChangeNickName())
						.makeAccountVisibleAttempts(accountInfoParameter.getMakeAccountVisibleAttempts())
						.alertOnTransactions(accountInfoParameter.getAlertOnTransactions())
						.alertOnLowBalanceAttempts(accountInfoParameter.getAlertOnLowBalanceAttempts()).build();
			}
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}
	}

	@Override
	public AccountInfoParameterDto deleteAccountInfoParameter() {
		try {
			AccountInfoParameter accountInfoParameter = accountInfoParameterRepo.findAll().get(0);
			accountInfoParameterRepo.delete(accountInfoParameter);
			return AccountInfoParameterDto.builder().nickNameMinLength(accountInfoParameter.getNickNameMinLength())
					.nickNameMaxLength(accountInfoParameter.getNickNameMaxLength())
					.noOfTimesUserCanChangeNickName(accountInfoParameter.getNoOfTimesUserCanChangeNickName())
					.makeAccountVisibleAttempts(accountInfoParameter.getMakeAccountVisibleAttempts())
					.alertOnTransactions(accountInfoParameter.getAlertOnTransactions())
					.alertOnLowBalanceAttempts(accountInfoParameter.getAlertOnLowBalanceAttempts()).build();
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}
	}

}
