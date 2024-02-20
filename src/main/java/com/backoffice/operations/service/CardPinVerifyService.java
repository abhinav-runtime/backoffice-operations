package com.backoffice.operations.service;

import org.springframework.stereotype.Service;

import com.backoffice.operations.payloads.EntityIdDTO;
import com.backoffice.operations.payloads.ValidationResultDTO;

@Service
public interface CardPinVerifyService {
	
	ValidationResultDTO verifyCardPin(EntityIdDTO entityIdDTO, String token);
	
}
