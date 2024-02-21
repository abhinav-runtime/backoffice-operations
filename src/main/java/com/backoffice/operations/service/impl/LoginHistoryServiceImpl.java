package com.backoffice.operations.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.LoginHistory;
import com.backoffice.operations.payloads.LoginFlagDTO;
import com.backoffice.operations.payloads.ValidationResultDTO;
import com.backoffice.operations.repository.LoginHistoryRepository;
import com.backoffice.operations.service.LoginHistoryService;

@Service
public class LoginHistoryServiceImpl implements LoginHistoryService {
	
	@Autowired
	private LoginHistoryRepository loginHistoryRepository;

	@Override
	public ValidationResultDTO saveLoginFlag(LoginFlagDTO loginFlagDTO) {
		
		ValidationResultDTO validationResultDTO = new ValidationResultDTO();
		LoginHistory loginHistory = new LoginHistory();
        loginHistory.setFlag(loginFlagDTO.isFlag());
        loginHistory.setLoginTimestamp(LocalDateTime.now());
        loginHistory.setUniqueKey(loginFlagDTO.getUniqueKey());
        loginHistory.setLang(loginFlagDTO.getLang());
        loginHistoryRepository.save(loginHistory);
        
        validationResultDTO.setStatus("Success");
		validationResultDTO.setMessage("Success");
        return validationResultDTO;
	}
}
