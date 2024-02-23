package com.backoffice.operations.service;

import org.springframework.stereotype.Service;

import com.backoffice.operations.payloads.EntityIdDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;

@Service
public interface CardPinVerifyService {
	
	GenericResponseDTO<Object> verifyCardPin(EntityIdDTO entityIdDTO, String token);
	
}
