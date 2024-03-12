package com.backoffice.operations.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.PasscodeParameter;
import com.backoffice.operations.payloads.PassCodeParameterDTO;
import com.backoffice.operations.repository.PassCodeParameterRepo;
import com.backoffice.operations.service.PassCodeParameterService;

@Service
public class PassCodeParameterServiceImp implements PassCodeParameterService {
	private static final Logger logger = LoggerFactory.getLogger(PassCodeParameterServiceImp.class);
	@Autowired
	private PassCodeParameterRepo parameterRepo;

	@Override
	public PassCodeParameterDTO getPasscodeParameter() {
		try {
			PasscodeParameter passcodeParameter = parameterRepo.findAll().get(0);
			return PassCodeParameterDTO.builder().passcodeLength(passcodeParameter.getPasscodeLength())
					.passCodeMaxAttempt(passcodeParameter.getPassCodeMaxAttempt())
					.lockoutDurationInMin(passcodeParameter.getLockoutDurationInMin())
					.changePasscodeMaxAttempt(passcodeParameter.getChangePasscodeMaxAttempt())
					.changePasscodeDurationInDays(passcodeParameter.getChangePasscodeDurationInDays()).build();
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}

	}

	@Override
	public PassCodeParameterDTO updatePasscodeParameter(PassCodeParameterDTO passcodeDto) {
		try {
			PasscodeParameter passcodeParameter = parameterRepo.findAll().get(0);
			if (passcodeDto.getPasscodeLength() != 0) {
				passcodeParameter.setPasscodeLength(passcodeDto.getPasscodeLength());
			}
			if (passcodeDto.getPassCodeMaxAttempt() != 0) {
				passcodeParameter.setPassCodeMaxAttempt(passcodeDto.getPassCodeMaxAttempt());
			}
			if (passcodeDto.getLockoutDurationInMin() != 0) {
				passcodeParameter.setLockoutDurationInMin(passcodeDto.getLockoutDurationInMin());
			}
			if (passcodeDto.getChangePasscodeMaxAttempt() != 0) {
				passcodeParameter.setChangePasscodeMaxAttempt(passcodeDto.getChangePasscodeMaxAttempt());
			}
			if (passcodeDto.getChangePasscodeDurationInDays() != 0) {
				passcodeParameter.setChangePasscodeDurationInDays(passcodeDto.getChangePasscodeDurationInDays());
			}
			passcodeParameter = parameterRepo.save(passcodeParameter);
			return PassCodeParameterDTO.builder().passcodeLength(passcodeParameter.getPasscodeLength())
					.passCodeMaxAttempt(passcodeParameter.getPassCodeMaxAttempt())
					.lockoutDurationInMin(passcodeParameter.getLockoutDurationInMin())
					.changePasscodeMaxAttempt(passcodeParameter.getChangePasscodeMaxAttempt())
					.changePasscodeDurationInDays(passcodeParameter.getChangePasscodeDurationInDays()).build();
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}
	}

	@Override
	public PassCodeParameterDTO createPassCodeParameter(PassCodeParameterDTO passcodeDto) {
		try {
			PasscodeParameter passcodeParameter = PasscodeParameter.builder()
					.passcodeLength(passcodeDto.getPasscodeLength())
					.passCodeMaxAttempt(passcodeDto.getPassCodeMaxAttempt())
					.lockoutDurationInMin(passcodeDto.getLockoutDurationInMin())
					.changePasscodeMaxAttempt(passcodeDto.getChangePasscodeMaxAttempt())
					.changePasscodeDurationInDays(passcodeDto.getChangePasscodeDurationInDays()).build();
			passcodeParameter = parameterRepo.save(passcodeParameter);
			return PassCodeParameterDTO.builder().passcodeLength(passcodeParameter.getPasscodeLength())
					.passCodeMaxAttempt(passcodeParameter.getPassCodeMaxAttempt())
					.lockoutDurationInMin(passcodeParameter.getLockoutDurationInMin())
					.changePasscodeMaxAttempt(passcodeParameter.getChangePasscodeMaxAttempt())
					.changePasscodeDurationInDays(passcodeParameter.getChangePasscodeDurationInDays()).build();
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}
	}

}
