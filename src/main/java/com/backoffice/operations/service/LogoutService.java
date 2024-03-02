package com.backoffice.operations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.backoffice.operations.entity.LoginHistory;
import com.backoffice.operations.entity.User;
import com.backoffice.operations.payloads.LogoutDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
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
	
	public GenericResponseDTO<Object> logout(LogoutDto logoutDto, String token) {
		String userEmail = jwtTokenProvider.getUsername(token);
		Optional<User> user = userRepository.findByEmail(userEmail);
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		
		if (user.isPresent()) {

			LoginHistory loginHistory = loginHistoryRepository.findFirstByUniqueKeyOrderByLoginTimestampDesc(logoutDto.getUniqueKey());

			loginHistory.setLogoutTimestamp(LocalDateTime.now());
			loginHistoryRepository.save(loginHistory);
			
			Map<String, String> data = new HashMap<>();
			responseDTO.setStatus("Success");
			responseDTO.setMessage("Success");
			data.put("uniqueKey",null);
			responseDTO.setData(data);
			return responseDTO;
		}
		
		Map<String, String> data = new HashMap<>();
		responseDTO.setStatus("Failure");
		responseDTO.setMessage("Something went wrong");
		data.put("uniqueKey",null);
		responseDTO.setData(data);
		return responseDTO;
	}	
}
