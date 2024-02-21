package com.backoffice.operations.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.CardEntity;
import com.backoffice.operations.entity.CardPinParameter;
import com.backoffice.operations.entity.SystemDetail;
import com.backoffice.operations.entity.User;
import com.backoffice.operations.payloads.EntityIdDTO;
import com.backoffice.operations.payloads.ValidationResultDTO;
import com.backoffice.operations.repository.CardPinParameterRepository;
import com.backoffice.operations.repository.CardRepository;
import com.backoffice.operations.repository.SystemDetailRepository;
import com.backoffice.operations.repository.UserRepository;
import com.backoffice.operations.security.JwtTokenProvider;
import com.backoffice.operations.service.CardPinVerifyService;

@Service
public class CardPinVerifyServiceImpl implements CardPinVerifyService {
	
	private Map<String, Integer> attemptsMap = new HashMap<>();
	private Map<String, LocalDateTime> cooldownMap = new HashMap<>();
	
	@Autowired
	private SystemDetailRepository systemDetailRepository;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CardRepository cardRepository;
	
	@Autowired
	private CardPinParameterRepository cardPinParameterRepository;
	
	
	@Override
	public ValidationResultDTO verifyCardPin(EntityIdDTO entityIdDTO, String token) {
		long id = 1;
		CardPinParameter cardPinParameter = cardPinParameterRepository.findById(id).orElse(null);
		int cardPinMaxAttempts = cardPinParameter.getCardPinMaximumAttempts();
		int cardPinCooldownPeriodSeconds = cardPinParameter.getCardPinCooldownInSec();

		int attempts = attemptsMap.getOrDefault(entityIdDTO.getUniqueKeySystem(), 0);
		
		String userEmail = jwtTokenProvider.getUsername(token);
		Optional<User> user = userRepository.findByEmail(userEmail);
		ValidationResultDTO validationResultDTO = new ValidationResultDTO();
		ValidationResultDTO.Data data = new ValidationResultDTO.Data();
		
		//If user present
		if (user.isPresent()) {
			EntityIdDTO.StoredCardPin storedPin = new EntityIdDTO.StoredCardPin();
			storedPin.setStoredCardPin("2233");
			CardEntity cardEntity = cardRepository.findByUniqueKeyCivilId(entityIdDTO.getUniqueKey());
			SystemDetail systemDetail = systemDetailRepository.findByUniqueKey(entityIdDTO.getUniqueKeySystem());
			
			//If CiviId UniqueKey and System UniqueKey is match
			if(Objects.nonNull(cardEntity) && Objects.nonNull(systemDetail))
			{
				//User is on cooldown
				 if (isUserOnCooldown(entityIdDTO.getUniqueKeySystem(),cardPinCooldownPeriodSeconds)) {
					 validationResultDTO.setStatus("Failure");
						validationResultDTO.setMessage("Maximum attempts reached. Please try again later.");
						data.setUniqueKey(null);
						validationResultDTO.setData(data);
						return validationResultDTO;
			        }
				 
				//If Card Pin is Empty or does not match
				if (!storedPin.getStoredCardPin().equals(entityIdDTO.getCardPin())) {
					attempts++;
		            attemptsMap.put(entityIdDTO.getUniqueKeySystem(), attempts);

					
					//Max Attempted Done
					if (attempts >= cardPinMaxAttempts){
						
						//Start coolDown for user
						cooldownMap.put(entityIdDTO.getUniqueKeySystem(), LocalDateTime.now());
						
						validationResultDTO.setStatus("Failure");
						validationResultDTO.setMessage("Maximum attempts reached. Please try again later.");
						data.setUniqueKey(null);
						validationResultDTO.setData(data);
						return validationResultDTO;
					} else {
						validationResultDTO.setStatus("Failure");
						validationResultDTO
						.setMessage("Incorrect Pin. Attempts left: " + (cardPinMaxAttempts - attempts));
						data.setUniqueKey(null);
						validationResultDTO.setData(data);
						return validationResultDTO;
					}
				}
				else {
					attemptsMap.remove(entityIdDTO.getUniqueKeySystem());
				}
				
				//If Card Pin is matching
				if(storedPin.getStoredCardPin().equals(entityIdDTO.getCardPin())) {
					
					validationResultDTO.setStatus("Success");
					validationResultDTO.setMessage("Success");
					data.setUniqueKey(null);
					validationResultDTO.setData(data);
					return validationResultDTO;	
				}
			}
		}	
		return null;
	}
	
	private boolean isUserOnCooldown(String uniqueKey, int cardPinCooldownPeriodSeconds) {
        LocalDateTime lastAttemptTime = cooldownMap.get(uniqueKey);
        if (lastAttemptTime == null) {
            return false; // User is not on cooldown
        }
		return lastAttemptTime.plusSeconds(cardPinCooldownPeriodSeconds).isAfter(LocalDateTime.now());
    }
}
