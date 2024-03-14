package com.backoffice.operations.service;

import com.backoffice.operations.payloads.BiometricAttemptParameterDto;

public interface BiometricAttemptService {
	BiometricAttemptParameterDto getBiometricAttemptParameter();

	BiometricAttemptParameterDto updateBiometricAttemptParameter(
			BiometricAttemptParameterDto biometricAttemptParameterDto);

	BiometricAttemptParameterDto createBiometricAttemptParameter(
			BiometricAttemptParameterDto biometricAttemptParameterDto);

	BiometricAttemptParameterDto deleteBiometricAttemptParameter();
}
