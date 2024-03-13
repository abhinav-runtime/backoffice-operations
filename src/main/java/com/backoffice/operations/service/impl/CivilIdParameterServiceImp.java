package com.backoffice.operations.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.CivilIdParameter;
import com.backoffice.operations.payloads.CivilIdParameterDTO;
import com.backoffice.operations.repository.CivilIdParameterRepository;
import com.backoffice.operations.service.CivilIdParameterService;

@Service
public class CivilIdParameterServiceImp implements CivilIdParameterService {
	private static final Logger logger = LoggerFactory.getLogger(CivilIdParameterServiceImp.class);
	@Autowired
	private CivilIdParameterRepository civilIdParameterRepository;

	@Override
	public CivilIdParameterDTO updateCivilIdParameter(CivilIdParameterDTO requestDto) {
		try {
			CivilIdParameter civilIdParameter = civilIdParameterRepository.findAll().get(0);
			if (requestDto.getMaxAttempts() != 0) {
				civilIdParameter.setCivilIdMaxAttempts(requestDto.getMaxAttempts());
			}
			if (requestDto.getCooldownTime() != 0) {
				civilIdParameter.setCivilIdCooldownInSec(requestDto.getCooldownTime());
			}
			if (requestDto.getOverrallMaxAttempts() != 0) {
				civilIdParameter.setOverrallMaxAttempts(requestDto.getOverrallMaxAttempts());
			}
			if (requestDto.getDeviceAttempts() != 0) {
				civilIdParameter.setDeviceAttempts(requestDto.getDeviceAttempts());
			}
			civilIdParameter = civilIdParameterRepository.save(civilIdParameter);
			return CivilIdParameterDTO.builder().maxAttempts(civilIdParameter.getCivilIdMaxAttempts())
					.cooldownTime(civilIdParameter.getCivilIdCooldownInSec())
					.overrallMaxAttempts(civilIdParameter.getOverrallMaxAttempts())
					.deviceAttempts(civilIdParameter.getDeviceAttempts()).build();
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}
	}

	@Override
	public CivilIdParameterDTO insertCivilIdParameter(CivilIdParameterDTO requestDto) {
		try {
			List<CivilIdParameter> civilIdParameterList = civilIdParameterRepository.findAll();
			if (civilIdParameterList.size() == 0) {
				CivilIdParameter civilIdParameter = CivilIdParameter.builder()
						.civilIdMaxAttempts(requestDto.getMaxAttempts())
						.civilIdCooldownInSec(requestDto.getCooldownTime())
						.overrallMaxAttempts(requestDto.getOverrallMaxAttempts())
						.deviceAttempts(requestDto.getDeviceAttempts()).build();
				civilIdParameter = civilIdParameterRepository.save(civilIdParameter);
				return CivilIdParameterDTO.builder().maxAttempts(civilIdParameter.getCivilIdMaxAttempts())
						.cooldownTime(civilIdParameter.getCivilIdCooldownInSec())
						.overrallMaxAttempts(civilIdParameter.getOverrallMaxAttempts())
						.deviceAttempts(civilIdParameter.getDeviceAttempts()).build();
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}

	}

	@Override
	public CivilIdParameterDTO getCivilIdParameter() {
		try {
			List<CivilIdParameter> civilIdParameterList = civilIdParameterRepository.findAll();
			if (civilIdParameterList == null || civilIdParameterList.size() == 0) {
				return null;
			}
			return CivilIdParameterDTO.builder().maxAttempts(civilIdParameterList.get(0).getCivilIdMaxAttempts())
					.cooldownTime(civilIdParameterList.get(0).getCivilIdCooldownInSec())
					.overrallMaxAttempts(civilIdParameterList.get(0).getOverrallMaxAttempts())
					.deviceAttempts(civilIdParameterList.get(0).getDeviceAttempts()).build();
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}

	}

	@Override
	public CivilIdParameterDTO deleteCivilIdParameter() {
		try {
			CivilIdParameter civilIdParameter = civilIdParameterRepository.findAll().get(0);
			civilIdParameterRepository.delete(civilIdParameter);
			return CivilIdParameterDTO.builder().maxAttempts(civilIdParameter.getCivilIdMaxAttempts())
					.cooldownTime(civilIdParameter.getCivilIdCooldownInSec())
					.overrallMaxAttempts(civilIdParameter.getOverrallMaxAttempts())
					.deviceAttempts(civilIdParameter.getDeviceAttempts()).build();
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}
	}
}
