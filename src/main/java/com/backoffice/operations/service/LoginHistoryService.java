package com.backoffice.operations.service;

import com.backoffice.operations.payloads.LoginFlagDTO;
import com.backoffice.operations.payloads.ValidationResultDTO;

public interface LoginHistoryService {
	
	ValidationResultDTO saveLoginFlag(LoginFlagDTO loginFlagDTO);

}
