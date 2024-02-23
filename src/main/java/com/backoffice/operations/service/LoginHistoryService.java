package com.backoffice.operations.service;

import com.backoffice.operations.payloads.LoginFlagDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;

public interface LoginHistoryService {
	
	GenericResponseDTO<Object> saveLoginFlag(LoginFlagDTO loginFlagDTO);

}
