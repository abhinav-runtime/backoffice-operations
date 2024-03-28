package com.backoffice.operations.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.OtpParameter;
import com.backoffice.operations.enums.AllowedChannels;
import com.backoffice.operations.enums.OtpLang;
import com.backoffice.operations.payloads.OtpParameterDTO;
import com.backoffice.operations.repository.OtpParameterRepository;
import com.backoffice.operations.service.OtpParameterService;
import com.backoffice.operations.utils.EnumUtils;

@Service
public class OtpParameterServiceImp implements OtpParameterService {
	private static final Logger logger = LoggerFactory.getLogger(OtpParameterServiceImp.class);
	@Autowired
	private OtpParameterRepository otpParameterRepository;

	@Override
	public OtpParameterDTO getOtpParameter() {
		try {
			OtpParameter otpParameter = otpParameterRepository.findAll().get(0);
			return OtpParameterDTO.builder().otpValidity(otpParameter.getOtpValidity())
					.otpMaxAttempts(otpParameter.getOtpMaxAttempts()).attemptTimeOut(otpParameter.getAttemptTimeOut())
					.otpLang(otpParameter.getOtpLang().name())
					.otpLength(otpParameter.getOtpLength())
					.allowedChannels(otpParameter.getAllowedChannels().name())
					.otpResend(otpParameter.getOtpResend())
					.otpResentTime(otpParameter.getOtpResentTime()).sessionExpiry(otpParameter.getSessionExpiry())
					.build();
		} catch (Exception e) {
			logger.error("Error: {}", e.getMessage());
			return null;
		}
	}

	@Override
	public OtpParameterDTO updateOtpParameter(OtpParameterDTO otpParameterDTO) {
		try {
			OtpParameter otpParameter = otpParameterRepository.findAll().get(0);
			if (otpParameterDTO.getOtpValidity() != 0) {
				otpParameter.setOtpValidity(otpParameterDTO.getOtpValidity());
			}
			if (otpParameterDTO.getOtpMaxAttempts() != 0) {
				otpParameter.setOtpMaxAttempts(otpParameterDTO.getOtpMaxAttempts());
			}
			if (otpParameterDTO.getAttemptTimeOut() != 0) {
				otpParameter.setAttemptTimeOut(otpParameterDTO.getAttemptTimeOut());
			}
			if (EnumUtils.isNamePresentInEnum(otpParameterDTO.getOtpLang(), OtpLang.class)) {
				otpParameter.setOtpLang(OtpLang.valueOf(otpParameterDTO.getOtpLang()));
			}
			if (otpParameterDTO.getOtpLength() != 0) {
				otpParameter.setOtpLength(otpParameterDTO.getOtpLength());
			}
			if (EnumUtils.isNamePresentInEnum(otpParameterDTO.getAllowedChannels(), AllowedChannels.class)) {
				otpParameter.setAllowedChannels(AllowedChannels.valueOf(otpParameterDTO.getAllowedChannels()));
			}						
			if (otpParameterDTO.getOtpResend() != 0) {
				otpParameter.setOtpResend(otpParameterDTO.getOtpResend());
			}
			if (otpParameterDTO.getOtpResentTime() != 0) {
				otpParameter.setOtpResentTime(otpParameterDTO.getOtpResentTime());
			}
			if (otpParameterDTO.getSessionExpiry() != 0) {
				otpParameter.setSessionExpiry(otpParameterDTO.getSessionExpiry());
			}
			otpParameter = otpParameterRepository.save(otpParameter);
			return OtpParameterDTO.builder().otpValidity(otpParameter.getOtpValidity())
					.otpMaxAttempts(otpParameter.getOtpMaxAttempts()).attemptTimeOut(otpParameter.getAttemptTimeOut())
					.otpLang(otpParameter.getOtpLang().name()).otpLength(otpParameter.getOtpLength())
					.allowedChannels(otpParameter.getAllowedChannels().name()).otpResend(otpParameter.getOtpResend())
					.otpResentTime(otpParameter.getOtpResentTime()).sessionExpiry(otpParameter.getSessionExpiry())
					.build();
		} catch (Exception e) {
			logger.error("Error: {}", e.getMessage());
			return null;
		}

	}

	@Override
	public OtpParameterDTO createOtpParameter(OtpParameterDTO otpParameterDTO) {
		try {
			List<OtpParameter> otpParameterList = otpParameterRepository.findAll();
			if (otpParameterList.size() == 0) {
				OtpParameter otpParameter = OtpParameter.builder().otpValidity(otpParameterDTO.getOtpValidity())
						.otpMaxAttempts(otpParameterDTO.getOtpMaxAttempts())
						.attemptTimeOut(otpParameterDTO.getAttemptTimeOut()).otpLang(OtpLang.valueOf(otpParameterDTO.getOtpLang()))
						.otpLength(otpParameterDTO.getOtpLength()).allowedChannels(AllowedChannels.valueOf(otpParameterDTO.getAllowedChannels()))
						.otpResend(otpParameterDTO.getOtpResend()).otpResentTime(otpParameterDTO.getOtpResentTime())
						.sessionExpiry(otpParameterDTO.getSessionExpiry()).build();
				otpParameter = otpParameterRepository.save(otpParameter);
				return OtpParameterDTO.builder().otpValidity(otpParameter.getOtpValidity())
						.otpMaxAttempts(otpParameter.getOtpMaxAttempts())
						.attemptTimeOut(otpParameter.getAttemptTimeOut()).otpLang(otpParameter.getOtpLang().name())
						.otpLength(otpParameter.getOtpLength()).allowedChannels(otpParameter.getAllowedChannels().name())
						.otpResend(otpParameter.getOtpResend()).otpResentTime(otpParameter.getOtpResentTime())
						.sessionExpiry(otpParameter.getSessionExpiry()).build();
			} else {
				return null;
			}

		} catch (Exception e) {
			logger.error("Error: {}", e.getMessage());
			return null;
		}
	}

	@Override
	public OtpParameterDTO deleteOtpParameter() {
		try {
			OtpParameter otpParameter = otpParameterRepository.findAll().get(0);
		otpParameterRepository.delete(otpParameter);
		return OtpParameterDTO.builder().otpValidity(otpParameter.getOtpValidity())
				.otpMaxAttempts(otpParameter.getOtpMaxAttempts()).attemptTimeOut(otpParameter.getAttemptTimeOut())
				.otpLang(otpParameter.getOtpLang().name()).otpLength(otpParameter.getOtpLength())
				.allowedChannels(otpParameter.getAllowedChannels().name()).otpResend(otpParameter.getOtpResend())
				.otpResentTime(otpParameter.getOtpResentTime()).sessionExpiry(otpParameter.getSessionExpiry()).build();
		} catch (Exception e) {
			logger.error("Error: {}", e.getMessage());
			return null;
		}
		
	}

}
