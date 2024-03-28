package com.backoffice.operations.service;

import com.backoffice.operations.payloads.CivilIdParameterDTO;

public interface CivilIdParameterService {
	CivilIdParameterDTO updateCivilIdParameter(CivilIdParameterDTO requestDto);
	CivilIdParameterDTO insertCivilIdParameter(CivilIdParameterDTO requestDto);
	CivilIdParameterDTO getCivilIdParameter();
	CivilIdParameterDTO deleteCivilIdParameter();
}
