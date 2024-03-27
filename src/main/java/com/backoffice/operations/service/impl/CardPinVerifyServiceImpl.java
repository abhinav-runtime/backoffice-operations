package com.backoffice.operations.service.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.CardEntity;
import com.backoffice.operations.entity.CardPinParameter;
import com.backoffice.operations.entity.User;
import com.backoffice.operations.payloads.EntityIdDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.CardPinParameterRepository;
import com.backoffice.operations.repository.CardRepository;
import com.backoffice.operations.repository.UserRepository;
import com.backoffice.operations.security.JwtTokenProvider;
import com.backoffice.operations.service.CardPinVerifyService;

@Service
public class CardPinVerifyServiceImpl implements CardPinVerifyService {
	
	private Map<String, Integer> attemptsMap = new HashMap<>();
	private Map<String, LocalDateTime> cooldownMap = new HashMap<>();
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CardRepository cardRepository;
	
	@Autowired
	private CardPinParameterRepository cardPinParameterRepository;
	
	
	@Override
	public GenericResponseDTO<Object> verifyCardPin(EntityIdDTO entityIdDTO, String token) {
		
		long id = 1;
		CardPinParameter cardPinParameter = cardPinParameterRepository.findById(id).orElse(null);
		int cardPinMaxAttempts = cardPinParameter.getCardPinMaximumAttempts();
		int cardPinCooldownPeriodSeconds = cardPinParameter.getCardPinCooldownInSec();
		
//		int attempts = attemptsMap.getOrDefault(entityIdDTO.getUniqueKey(), 0);
		
		String userEmail = jwtTokenProvider.getUsername(token);
		Optional<User> user = userRepository.findByEmail(userEmail);
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
//		ValidationResultDTO validationResultDTO = new ValidationResultDTO();
		
		//If user present
		if (user.isPresent()) {
			EntityIdDTO.StoredCardPin storedPin = new EntityIdDTO.StoredCardPin();
			storedPin.setStoredCardPin("2233");
			CardEntity cardEntity = cardRepository.findByUniqueKeyCivilId(entityIdDTO.getUniqueKey());
			int attempts = cardEntity.getAttempts();
			
			//If CiviId UniqueKey and System UniqueKey is match
			if(Objects.nonNull(cardEntity))
			{
				//User is on cooldown
				 if (isUserOnCooldown(entityIdDTO.getUniqueKey(),cardPinCooldownPeriodSeconds)) {
					 responseDTO.setStatus("Failure");
					 responseDTO.setMessage("Maximum attempts reached. Please try again later.");
						return responseDTO;
			        }

				 //If Pin length is not correct
				if (null == entityIdDTO.getCardPin() || entityIdDTO.getCardPin().length() > cardPinParameter.getPinLength()) {
					responseDTO.setStatus("Failure");
					responseDTO.setMessage("Pin Length for the card is incorrect. Please try again later.");
					return responseDTO;
				}

				//If Max Sequential Digit Limit is exceeds
				if (maxSequentialDigits(entityIdDTO.getCardPin()) > cardPinParameter.getSequentialDigits()){
					responseDTO.setStatus("Failure");
					responseDTO.setMessage("Number of maximum sequential digits in PIN Exceeded. Please try again later.");
					return responseDTO;
				}

				if (ChronoUnit.SECONDS.between(cardEntity.getLastMaxAttemptTime(),
						LocalDateTime.now()) < cardPinParameter.getSessionExpiry()) {
					responseDTO.setStatus("Failure");
					responseDTO.setMessage("Session Is Blocked. Please try again later.");
					return responseDTO;
				}

				if (ChronoUnit.SECONDS.between(cardEntity.getLastMaxAttemptTime(),
						LocalDateTime.now()) < cardPinParameter.getCardPinCooldownInSec()) {
					responseDTO.setStatus("Failure");
					responseDTO.setMessage("Session Is Blocked. Please try again later.");
					return responseDTO;
				}

				//If Number of maximum repetitive digits in PIN (111) Excceds
				if (maxRepetitiveDigits(entityIdDTO.getCardPin()) > cardPinParameter.getRepetitiveDigits()){
					responseDTO.setStatus("Failure");
					responseDTO.setMessage("Number of maximum repetitive digits in PIN Exceeded. Please try again later.");
					return responseDTO;
				}

				//If Card Pin is Empty or does not match
				if (!storedPin.getStoredCardPin().equals(entityIdDTO.getCardPin()))
				{
					attempts++;
		            attemptsMap.put(entityIdDTO.getUniqueKey(), attempts);
					cardEntity.setAttempts(attempts);
					cardEntity.setLastAttemptTime(LocalDateTime.now());
					cardRepository.save(cardEntity);
					
					//Max Attempted Done
					if (attempts >= cardPinMaxAttempts){
						
						//Start coolDown for user
						cooldownMap.put(entityIdDTO.getUniqueKey(), LocalDateTime.now());
						cardEntity.setLastMaxAttemptTime(LocalDateTime.now());
						cardRepository.save(cardEntity);
						responseDTO.setStatus("Failure");
						responseDTO.setMessage("Maximum attempts reached. Please try again later.");
						return responseDTO;
					} else {
						responseDTO.setStatus("Failure");
						responseDTO
						.setMessage("Incorrect Pin. Attempts left: " + (cardPinMaxAttempts - attempts));
						return responseDTO;
					}
				}
				else {
					attemptsMap.remove(entityIdDTO.getUniqueKey());
				}
				
				//If Card Pin is matching
				if(storedPin.getStoredCardPin().equals(entityIdDTO.getCardPin())) {
					
					responseDTO.setStatus("Success");
					responseDTO.setMessage("Success");
					return responseDTO;	
				}
			}
		}	
		responseDTO.setStatus("Failure");
		responseDTO.setMessage("Something went wrong.");
		return responseDTO;
	}
	
	private boolean isUserOnCooldown(String uniqueKey, int cardPinCooldownPeriodSeconds) {
        LocalDateTime lastAttemptTime = cooldownMap.get(uniqueKey);
        if (lastAttemptTime == null) {
            return false; // User is not on cooldown
        }
		return lastAttemptTime.plusSeconds(cardPinCooldownPeriodSeconds).isAfter(LocalDateTime.now());
    }

	private int maxSequentialDigits(String pin) {
		return Arrays.stream(pin.split("(?<=(.))(?!\\1)"))
				.mapToInt(String::length)
				.max()
				.orElse(0);
	}

	private int maxRepetitiveDigits(String pin) {
		Matcher matcher = Pattern.compile("(\\d)\\1*").matcher(pin);
		int maxCount = 0;

		while (matcher.find()) {
			maxCount += matcher.group().length();
		}

		return maxCount;
	}
}
