package com.backoffice.operations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.backoffice.operations.entity.LoginHistory;
import com.backoffice.operations.entity.User;
import com.backoffice.operations.payloads.LogoutDto;
import com.backoffice.operations.payloads.ValidationResultDTO;
import com.backoffice.operations.repository.LoginHistoryRepository;
import com.backoffice.operations.repository.UserRepository;
import com.backoffice.operations.security.JwtTokenProvider;

@Service
public class LogoutService {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private LoginHistoryRepository loginHistoryRepository;
	
	public ValidationResultDTO logout(LogoutDto logoutDto, String token) {
		String userEmail = jwtTokenProvider.getUsername(token);
		Optional<User> user = userRepository.findByEmail(userEmail);
		List<LoginHistory> obj = loginHistoryRepository.findByUniqueKey(logoutDto.getUniqueKey());
		ValidationResultDTO validationResultDTO = new ValidationResultDTO();
		
		if (user.isPresent() && obj != null) 
		{
			LoginHistory loginHistory = loginHistoryRepository.findFirstByOrderByLoginTimestampDesc();
			
				loginHistory.setLogoutTimestamp(LocalDateTime.now());
				loginHistoryRepository.save(loginHistory);
			
				validationResultDTO.setStatus("Success");
				validationResultDTO.setMessage("Success");
				return validationResultDTO;
		}
		validationResultDTO.setStatus("Failure");
		validationResultDTO.setMessage("Something went wrong");
		return validationResultDTO;
	}	
}
