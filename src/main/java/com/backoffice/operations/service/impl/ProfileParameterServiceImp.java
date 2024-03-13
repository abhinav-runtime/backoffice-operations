package com.backoffice.operations.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.ProfileParameter;
import com.backoffice.operations.payloads.ProfileParameterDto;
import com.backoffice.operations.repository.ProfileParameterRepo;
import com.backoffice.operations.service.ProfileParameterService;

@Service
public class ProfileParameterServiceImp implements ProfileParameterService {
	private static final Logger logger = LoggerFactory.getLogger(ProfileParameterServiceImp.class);
	@Autowired
	private ProfileParameterRepo profileParameterRepository;

	@Override
	public ProfileParameterDto getProfileParameter() {
		try {
			ProfileParameter profileParameter = profileParameterRepository.findAll().get(0);
			return ProfileParameterDto.builder()
					.mobileChangeAttemptLimit(profileParameter.getMobileChangeAttemptLimit())
					.mobileChangeLockoutDurationInSec(profileParameter.getMobileChangeLockoutDurationInSec())
					.mobileNumberChangedLockoutDurationInDay(
							profileParameter.getMobileNumberChangedLockoutDurationInDay())
					.emailIdChangeAttemptLimit(profileParameter.getEmailIdChangeAttemptLimit())
					.emailIdVerificationLinkResendAttempts(profileParameter.getEmailIdVerificationLinkResendAttempts())
					.emailIdVerificationLinkTimeoutInSec(profileParameter.getEmailIdVerificationLinkTimeoutInSec())
					.emailIdChangeLockoutDurationInSec(profileParameter.getEmailIdChangeLockoutDurationInSec())
					.emailIdChangedDurationInDay(profileParameter.getEmailIdChangedDurationInDay())
					.emailStatementFlagAttempts(profileParameter.getEmailStatementFlagAttempts()).build();

		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}
	}

	@Override
	public ProfileParameterDto updateProfileParameter(ProfileParameterDto profileParameterDto) {
		try {
			ProfileParameter profileParameter = profileParameterRepository.findAll().get(0);
			if (profileParameterDto.getMobileChangeAttemptLimit() != 0) {
				profileParameter.setMobileChangeAttemptLimit(profileParameterDto.getMobileChangeAttemptLimit());
			}
			if (profileParameterDto.getMobileChangeLockoutDurationInSec() != 0) {
				profileParameter
						.setMobileChangeLockoutDurationInSec(profileParameterDto.getMobileChangeLockoutDurationInSec());
			}
			if (profileParameterDto.getMobileNumberChangedLockoutDurationInDay() != 0) {
				profileParameter.setMobileNumberChangedLockoutDurationInDay(
						profileParameterDto.getMobileNumberChangedLockoutDurationInDay());
			}
			if (profileParameterDto.getEmailIdChangeAttemptLimit() != 0) {
				profileParameter.setEmailIdChangeAttemptLimit(profileParameterDto.getEmailIdChangeAttemptLimit());
			}
			if (profileParameterDto.getEmailIdVerificationLinkResendAttempts() != 0) {
				profileParameter.setEmailIdVerificationLinkResendAttempts(
						profileParameterDto.getEmailIdVerificationLinkResendAttempts());
			}
			if (profileParameterDto.getEmailIdVerificationLinkTimeoutInSec() != 0) {
				profileParameter.setEmailIdVerificationLinkTimeoutInSec(
						profileParameterDto.getEmailIdVerificationLinkTimeoutInSec());
			}
			if (profileParameterDto.getEmailIdChangeLockoutDurationInSec() != 0) {
				profileParameter.setEmailIdChangeLockoutDurationInSec(
						profileParameterDto.getEmailIdChangeLockoutDurationInSec());
			}
			if (profileParameterDto.getEmailIdChangedDurationInDay() != 0) {
				profileParameter.setEmailIdChangedDurationInDay(profileParameterDto.getEmailIdChangedDurationInDay());
			}
			if (profileParameterDto.getEmailStatementFlagAttempts() != 0) {
				profileParameter.setEmailStatementFlagAttempts(profileParameterDto.getEmailStatementFlagAttempts());
			}
			profileParameter = profileParameterRepository.save(profileParameter);
			return ProfileParameterDto.builder()
					.mobileChangeAttemptLimit(profileParameter.getMobileChangeAttemptLimit())
					.mobileChangeLockoutDurationInSec(profileParameter.getMobileChangeLockoutDurationInSec())
					.mobileNumberChangedLockoutDurationInDay(
							profileParameter.getMobileNumberChangedLockoutDurationInDay())
					.emailIdChangeAttemptLimit(profileParameter.getEmailIdChangeAttemptLimit())
					.emailIdVerificationLinkResendAttempts(profileParameter.getEmailIdVerificationLinkResendAttempts())
					.emailIdVerificationLinkTimeoutInSec(profileParameter.getEmailIdVerificationLinkTimeoutInSec())
					.emailIdChangeLockoutDurationInSec(profileParameter.getEmailIdChangeLockoutDurationInSec())
					.emailIdChangedDurationInDay(profileParameter.getEmailIdChangedDurationInDay())
					.emailStatementFlagAttempts(profileParameter.getEmailStatementFlagAttempts()).build();
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}
	}

	@Override
	public ProfileParameterDto createProfileParameter(ProfileParameterDto profileParameterDto) {
		try {
			List<ProfileParameter> profileParameterList = profileParameterRepository.findAll();
			if (profileParameterList.size() != 0) {
				return null;
			} else {
				ProfileParameter profileParameter = ProfileParameter.builder()
						.mobileChangeAttemptLimit(profileParameterDto.getMobileChangeAttemptLimit())
						.mobileChangeLockoutDurationInSec(profileParameterDto.getMobileChangeLockoutDurationInSec())
						.mobileNumberChangedLockoutDurationInDay(
								profileParameterDto.getMobileNumberChangedLockoutDurationInDay())
						.emailIdChangeAttemptLimit(profileParameterDto.getEmailIdChangeAttemptLimit())
						.emailIdVerificationLinkResendAttempts(
								profileParameterDto.getEmailIdVerificationLinkResendAttempts())
						.emailIdVerificationLinkTimeoutInSec(
								profileParameterDto.getEmailIdVerificationLinkTimeoutInSec())
						.emailIdChangeLockoutDurationInSec(profileParameterDto.getEmailIdChangeLockoutDurationInSec())
						.emailIdChangedDurationInDay(profileParameterDto.getEmailIdChangedDurationInDay())
						.emailStatementFlagAttempts(profileParameterDto.getEmailStatementFlagAttempts()).build();
				profileParameter = profileParameterRepository.save(profileParameter);
				return ProfileParameterDto.builder()
						.mobileChangeAttemptLimit(profileParameter.getMobileChangeAttemptLimit())
						.mobileChangeLockoutDurationInSec(profileParameter.getMobileChangeLockoutDurationInSec())
						.mobileNumberChangedLockoutDurationInDay(
								profileParameter.getMobileNumberChangedLockoutDurationInDay())
						.emailIdChangeAttemptLimit(profileParameter.getEmailIdChangeAttemptLimit())
						.emailIdVerificationLinkResendAttempts(
								profileParameter.getEmailIdVerificationLinkResendAttempts())
						.emailIdVerificationLinkTimeoutInSec(profileParameter.getEmailIdVerificationLinkTimeoutInSec())
						.emailIdChangeLockoutDurationInSec(profileParameter.getEmailIdChangeLockoutDurationInSec())
						.emailIdChangedDurationInDay(profileParameter.getEmailIdChangedDurationInDay())
						.emailStatementFlagAttempts(profileParameter.getEmailStatementFlagAttempts()).build();
			}
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}
	}

}
