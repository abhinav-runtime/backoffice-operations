package com.backoffice.operations.service;

import com.backoffice.operations.payloads.ProfileParameterDto;

public interface ProfileParameterService {
	ProfileParameterDto getProfileParameter();
	ProfileParameterDto updateProfileParameter(ProfileParameterDto profileParameterDto);
	ProfileParameterDto createProfileParameter(ProfileParameterDto profileParameterDto);
}
