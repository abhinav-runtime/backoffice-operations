package com.backoffice.operations.service;

import org.springframework.stereotype.Service;

import com.backoffice.operations.payloads.CardPinVerifyDTO;
import com.backoffice.operations.payloads.ValidationResultDTO;

@Service
public interface CardPinVerifyService {
	
	ValidationResultDTO verifyCardPin(CardPinVerifyDTO cardPinVerifyDTO, String token);
	
}
