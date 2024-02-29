package com.backoffice.operations.service;

import com.backoffice.operations.payloads.BORegisterDTO;
import com.backoffice.operations.payloads.BOSuspendUserDTO;
import com.backoffice.operations.payloads.LoginDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;

public interface BOAuthService {
	GenericResponseDTO<Object> login(LoginDto loginDto);
	GenericResponseDTO<Object> register(BORegisterDTO boRegisterDTO);
	GenericResponseDTO<Object> suspendUser(BOSuspendUserDTO userDto);
	GenericResponseDTO<Object> logout();
}
