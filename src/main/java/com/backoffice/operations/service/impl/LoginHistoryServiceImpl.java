package com.backoffice.operations.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.LoginHistory;
import com.backoffice.operations.payloads.LoginFlagDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.LoginHistoryRepository;
import com.backoffice.operations.service.LoginHistoryService;

@Service
public class LoginHistoryServiceImpl implements LoginHistoryService {
	
	@Autowired
	private LoginHistoryRepository loginHistoryRepository;

	@Override
	public GenericResponseDTO<Object> saveLoginFlag(LoginFlagDTO loginFlagDTO) {
		
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		LoginHistory loginHistory = new LoginHistory();
        loginHistory.setFlag(loginFlagDTO.isFlag());
        loginHistory.setLoginTimestamp(LocalDateTime.now());
        loginHistory.setUniqueKey(loginFlagDTO.getUniqueKey());
        loginHistory.setLang(loginFlagDTO.getLang());
        loginHistoryRepository.save(loginHistory);
        
        responseDTO.setStatus("Success");
        responseDTO.setData(null);
        responseDTO.setMessage("Success");
        return responseDTO;
	}

}
