package com.backoffice.operations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import com.backoffice.operations.entity.UserLoginDetails;
import com.backoffice.operations.entity.User;
import com.backoffice.operations.payloads.LogoutDto;
import com.backoffice.operations.payloads.ValidationResultDTO;
import com.backoffice.operations.repository.UserLoginDetailsRepository;
import com.backoffice.operations.repository.UserRepository;
import com.backoffice.operations.security.JwtTokenProvider;

@Service
public class LogoutService {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserLoginDetailsRepository logRepository;
	
	public ValidationResultDTO logout(LogoutDto logoutDto, String token) {
		String userEmail = jwtTokenProvider.getUsername(token);
		Optional<User> user = userRepository.findByEmail(userEmail);
		ValidationResultDTO validationResultDTO = new ValidationResultDTO();
		ValidationResultDTO.Data data = new ValidationResultDTO.Data();
		
		if (user.isPresent()) {
			
			UserLoginDetails userLoginDetails = logRepository.findById(logoutDto.getId()).orElseThrow(() -> new RuntimeException("Session not found"));
			userLoginDetails.setLogoutTime(LocalDateTime.now());
			logRepository.save(userLoginDetails);
			
			validationResultDTO.setStatus("Success");
			validationResultDTO.setMessage("Success");
			data.setUniqueKey(null);
			validationResultDTO.setData(data);
			return validationResultDTO;
		}
		
		validationResultDTO.setStatus("Failure");
		validationResultDTO.setMessage("Something went wrong");
		data.setUniqueKey(null);
		validationResultDTO.setData(data);
		return validationResultDTO;
	}	
}
