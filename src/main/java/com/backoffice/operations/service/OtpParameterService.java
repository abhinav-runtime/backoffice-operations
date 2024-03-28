package com.backoffice.operations.service;

import com.backoffice.operations.payloads.OtpParameterDTO;

public interface OtpParameterService {
	public OtpParameterDTO getOtpParameter();
	public OtpParameterDTO updateOtpParameter(OtpParameterDTO otpParameterDTO);
	public OtpParameterDTO createOtpParameter(OtpParameterDTO otpParameterDTO);
	public OtpParameterDTO deleteOtpParameter();
}
