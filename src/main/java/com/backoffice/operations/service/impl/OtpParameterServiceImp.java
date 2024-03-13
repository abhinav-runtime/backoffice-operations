package com.backoffice.operations.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.OtpParameter;
import com.backoffice.operations.payloads.OtpParameterDTO;
import com.backoffice.operations.repository.OtpParameterRepository;
import com.backoffice.operations.service.OtpParameterService;

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
					.otpLang(otpParameter.getOtpLang()).otpLength(otpParameter.getOtpLength())
					.allowedChannels(otpParameter.getAllowedChannels()).otpResend(otpParameter.getOtpResend())
					.otpResentTime(otpParameter.getOtpResentTime()).sessionExpiry(otpParameter.getSessionExpiry())
					.build();
		} catch (Exception e) {
			logger.error("Error: {}", e.getMessage());
			return null;
		}
	}

	@Override
	public OtpParameterDTO updateOtpParameter(OtpParameterDTO otpParameterDTO) {
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
		if (otpParameterDTO.getOtpLang() != null) {
			otpParameter.setOtpLang(otpParameterDTO.getOtpLang());
		}
		if (otpParameterDTO.getOtpLength() != 0) {
			otpParameter.setOtpLength(otpParameterDTO.getOtpLength());
		}
		if (otpParameterDTO.getAllowedChannels() != null) {
			otpParameter.setAllowedChannels(otpParameterDTO.getAllowedChannels());
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
				.otpLang(otpParameter.getOtpLang()).otpLength(otpParameter.getOtpLength())
				.allowedChannels(otpParameter.getAllowedChannels()).otpResend(otpParameter.getOtpResend())
				.otpResentTime(otpParameter.getOtpResentTime()).sessionExpiry(otpParameter.getSessionExpiry()).build();
	}

	@Override
	public OtpParameterDTO createOtpParameter(OtpParameterDTO otpParameterDTO) {
		List<OtpParameter> otpParameterList = otpParameterRepository.findAll();
		if (otpParameterList.size() == 0) {
			OtpParameter otpParameter = OtpParameter.builder().otpValidity(otpParameterDTO.getOtpValidity())
					.otpMaxAttempts(otpParameterDTO.getOtpMaxAttempts())
					.attemptTimeOut(otpParameterDTO.getAttemptTimeOut()).otpLang(otpParameterDTO.getOtpLang())
					.otpLength(otpParameterDTO.getOtpLength()).allowedChannels(otpParameterDTO.getAllowedChannels())
					.otpResend(otpParameterDTO.getOtpResend()).otpResentTime(otpParameterDTO.getOtpResentTime())
					.sessionExpiry(otpParameterDTO.getSessionExpiry()).build();
			otpParameter = otpParameterRepository.save(otpParameter);
			return OtpParameterDTO.builder().otpValidity(otpParameter.getOtpValidity())
					.otpMaxAttempts(otpParameter.getOtpMaxAttempts()).attemptTimeOut(otpParameter.getAttemptTimeOut())
					.otpLang(otpParameter.getOtpLang()).otpLength(otpParameter.getOtpLength())
					.allowedChannels(otpParameter.getAllowedChannels()).otpResend(otpParameter.getOtpResend())
					.otpResentTime(otpParameter.getOtpResentTime()).sessionExpiry(otpParameter.getSessionExpiry())
					.build();
		}
		return null;
	}

}
