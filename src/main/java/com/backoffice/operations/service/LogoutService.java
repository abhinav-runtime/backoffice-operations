package com.backoffice.operations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import com.backoffice.operations.entity.CardEntity;
import com.backoffice.operations.entity.LogoutEntity;
import com.backoffice.operations.entity.User;
import com.backoffice.operations.payloads.LogoutDto;
import com.backoffice.operations.payloads.ValidationResultDTO;
import com.backoffice.operations.repository.CardRepository;
import com.backoffice.operations.repository.LogoutRepository;
import com.backoffice.operations.repository.UserRepository;
import com.backoffice.operations.security.JwtTokenProvider;

@Service
public class LogoutService {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CardRepository cardRepository;
	
	@Autowired
	private LogoutRepository logoutRepository;
	
	public ValidationResultDTO logout(LogoutDto logoutDto, String token) {
		String userEmail = jwtTokenProvider.getUsername(token);
		Optional<User> user = userRepository.findByEmail(userEmail);
		LogoutEntity logoutEntity = new LogoutEntity();
		ValidationResultDTO validationResultDTO = new ValidationResultDTO();
		ValidationResultDTO.Data data = new ValidationResultDTO.Data();
		
		if (user.isPresent()) {
			CardEntity cardEntity = cardRepository.findByUniqueKeyCivilId(logoutDto.getUniqueKey());
			if (Objects.nonNull(cardEntity)) {
				logoutEntity.setUniqueKey(logoutDto.getUniqueKey());
				logoutEntity.setLogoutTime(LocalDateTime.now());
				logoutRepository.save(logoutEntity);
				
				validationResultDTO.setStatus("Success");
				validationResultDTO.setMessage("Success");
				data.setUniqueKey(null);
				validationResultDTO.setData(data);
				return validationResultDTO;
			}
		}
		
		validationResultDTO.setStatus("Failure");
		validationResultDTO.setMessage("Something went wrong");
		data.setUniqueKey(null);
		validationResultDTO.setData(data);
		return validationResultDTO;
	}	
}
