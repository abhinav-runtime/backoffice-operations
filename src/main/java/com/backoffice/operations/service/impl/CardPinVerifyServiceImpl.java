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
import com.backoffice.operations.entity.SystemDetail;
import com.backoffice.operations.entity.User;
import com.backoffice.operations.payloads.CardPinVerifyDTO;
import com.backoffice.operations.payloads.ValidationResultDTO;
import com.backoffice.operations.repository.CardRepository;
import com.backoffice.operations.repository.SystemDetailRepository;
import com.backoffice.operations.repository.UserRepository;
import com.backoffice.operations.security.JwtTokenProvider;
import com.backoffice.operations.service.CardPinVerifyService;

@Service
public class CardPinVerifyServiceImpl implements CardPinVerifyService {

	@Value("${cardPin.maxAttempts}")
	private int cardPinMaxAttempts;
	
	@Value("${cardPin.cooldownPeriodSeconds}")
	private int cardPinCooldownPeriodSeconds;
	
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
	
	@Override
	public ValidationResultDTO verifyCardPin(CardPinVerifyDTO cardPinVerifyDTO, String token) {
		int attempts = attemptsMap.getOrDefault(cardPinVerifyDTO.getUniqueKeySystem(), 0);
		
		String userEmail = jwtTokenProvider.getUsername(token);
		Optional<User> user = userRepository.findByEmail(userEmail);
		ValidationResultDTO validationResultDTO = new ValidationResultDTO();
		ValidationResultDTO.Data data = new ValidationResultDTO.Data();
		
		//If user present
		if (user.isPresent()) {
			CardPinVerifyDTO.StoredCardPin storedPin = new CardPinVerifyDTO.StoredCardPin();
			storedPin.setStoredCardPin("2233");
			CardEntity cardEntity = cardRepository.findByUniqueKeyCivilId(cardPinVerifyDTO.getUniqueKeyCivilID());
			SystemDetail systemDetail = systemDetailRepository.findByUniqueKey(cardPinVerifyDTO.getUniqueKeySystem());
			
			//If CiviId UniqueKey and System UniqueKey is match
			if(Objects.nonNull(cardEntity) && Objects.nonNull(systemDetail))
			{
				//User is on cooldown
				 if (isUserOnCooldown(cardPinVerifyDTO.getUniqueKeySystem())) {
					 validationResultDTO.setStatus("Failure");
						validationResultDTO.setMessage("Maximum attempts reached. Please try again later.");
						data.setUniqueKey(null);
						validationResultDTO.setData(data);
						return validationResultDTO;
			        }
				 
				//If Card Pin is Empty or does not match
				if (!storedPin.getStoredCardPin().equals(cardPinVerifyDTO.getCardPin())) {
					attempts++;
		            attemptsMap.put(cardPinVerifyDTO.getUniqueKeySystem(), attempts);

					
					//Max Attempted Done
					if (attempts >= cardPinMaxAttempts){
						
						//Start coolDown for user
						cooldownMap.put(cardPinVerifyDTO.getUniqueKeySystem(), LocalDateTime.now());
						
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
					attemptsMap.remove(cardPinVerifyDTO.getUniqueKeySystem());
				}
				
				//If Card Pin is matching
				if(storedPin.getStoredCardPin().equals(cardPinVerifyDTO.getCardPin())) {
					
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
	
	private boolean isUserOnCooldown(String uniqueKey) {
        LocalDateTime lastAttemptTime = cooldownMap.get(uniqueKey);
        if (lastAttemptTime == null) {
            return false; // User is not on cooldown
        }
		return lastAttemptTime.plusSeconds(cardPinCooldownPeriodSeconds).isAfter(LocalDateTime.now());
    }
}
