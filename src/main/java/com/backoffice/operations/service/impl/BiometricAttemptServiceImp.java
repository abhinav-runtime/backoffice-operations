package com.backoffice.operations.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.BiometricAttemptParameter;
import com.backoffice.operations.payloads.BiometricAttemptParameterDto;
import com.backoffice.operations.repository.BiometricAttemptParameterRepo;
import com.backoffice.operations.service.BiometricAttemptService;

@Service
public class BiometricAttemptServiceImp implements BiometricAttemptService {
	private static final Logger logger = LoggerFactory.getLogger(BiometricAttemptServiceImp.class);
	@Autowired
	private BiometricAttemptParameterRepo biometricAttemptParameterRepo;

	@Override
	public BiometricAttemptParameterDto getBiometricAttemptParameter() {
		try {
			BiometricAttemptParameter biometricAttemptParameter = biometricAttemptParameterRepo.findAll().get(0);
			return BiometricAttemptParameterDto.builder().attemptLimit(biometricAttemptParameter.getAttemptLimit())
					.lockoutDuration(biometricAttemptParameter.getLockoutDuration())
					.biometricEnable(biometricAttemptParameter.getBiometricEnable()).build();
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}
	}

	@Override
	public BiometricAttemptParameterDto updateBiometricAttemptParameter(
			BiometricAttemptParameterDto biometricAttemptParameterDto) {
		try {
			BiometricAttemptParameter biometricAttemptParameter = biometricAttemptParameterRepo.findAll().get(0);
			if (biometricAttemptParameterDto.getAttemptLimit() != 0) {
				biometricAttemptParameter.setAttemptLimit(biometricAttemptParameterDto.getAttemptLimit());
			}
			if (biometricAttemptParameterDto.getLockoutDuration() != 0) {
				biometricAttemptParameter.setLockoutDuration(biometricAttemptParameterDto.getLockoutDuration());
			}
			if (biometricAttemptParameterDto.getBiometricEnable() != 0) {
				biometricAttemptParameter.setBiometricEnable(biometricAttemptParameterDto.getBiometricEnable());
			}
			biometricAttemptParameter = biometricAttemptParameterRepo.save(biometricAttemptParameter);
			return BiometricAttemptParameterDto.builder().id(biometricAttemptParameter.getId())
					.attemptLimit(biometricAttemptParameter.getAttemptLimit())
					.lockoutDuration(biometricAttemptParameter.getLockoutDuration())
					.biometricEnable(biometricAttemptParameter.getBiometricEnable()).build();
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}
	}

	@Override
	public BiometricAttemptParameterDto createBiometricAttemptParameter(
			BiometricAttemptParameterDto biometricAttemptParameterDto) {
		try {
			List<BiometricAttemptParameter> biometricAttemptParameter = biometricAttemptParameterRepo.findAll();
			if (biometricAttemptParameter.size() > 0) {
				return null;
			} else {
				BiometricAttemptParameter biometricAttemptParameterEntity = BiometricAttemptParameter.builder()
						.attemptLimit(biometricAttemptParameterDto.getAttemptLimit())
						.lockoutDuration(biometricAttemptParameterDto.getLockoutDuration())
						.biometricEnable(biometricAttemptParameterDto.getBiometricEnable()).build();
				biometricAttemptParameterEntity = biometricAttemptParameterRepo.save(biometricAttemptParameterEntity);
				return BiometricAttemptParameterDto.builder().id(biometricAttemptParameterEntity.getId())
						.attemptLimit(biometricAttemptParameterEntity.getAttemptLimit())
						.lockoutDuration(biometricAttemptParameterEntity.getLockoutDuration())
						.biometricEnable(biometricAttemptParameterEntity.getBiometricEnable()).build();
			}
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}

	}

	@Override
	public BiometricAttemptParameterDto deleteBiometricAttemptParameter() {
		try {
			BiometricAttemptParameter biometricAttemptParameter = biometricAttemptParameterRepo.findAll().get(0);
			biometricAttemptParameterRepo.delete(biometricAttemptParameter);
			return BiometricAttemptParameterDto.builder().id(biometricAttemptParameter.getId())
					.attemptLimit(biometricAttemptParameter.getAttemptLimit())
					.lockoutDuration(biometricAttemptParameter.getLockoutDuration())
					.biometricEnable(biometricAttemptParameter.getBiometricEnable()).build();
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}
	}

}
