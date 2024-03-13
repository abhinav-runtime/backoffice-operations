package com.backoffice.operations.service;

import com.backoffice.operations.payloads.PassCodeParameterDTO;

public interface PassCodeParameterService {
	public PassCodeParameterDTO getPasscodeParameter();
	public PassCodeParameterDTO updatePasscodeParameter(PassCodeParameterDTO passcodeDto);
	public PassCodeParameterDTO createPassCodeParameter(PassCodeParameterDTO passcodeDto);
}
