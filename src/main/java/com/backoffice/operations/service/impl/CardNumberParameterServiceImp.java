package com.backoffice.operations.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.CardNumberParameter;
import com.backoffice.operations.payloads.CardNumberParameterDTO;
import com.backoffice.operations.repository.CardNumberParameterRepo;
import com.backoffice.operations.service.CardNumberParameterService;

@Service
public class CardNumberParameterServiceImp implements CardNumberParameterService {
	private static final Logger logger = LoggerFactory.getLogger(CardNumberParameterServiceImp.class);
	@Autowired
	private CardNumberParameterRepo cardNumberParameterRepo;

	@Override
	public CardNumberParameterDTO getCardNumberParameter() {
		try {
			CardNumberParameter cardNumberParameter = cardNumberParameterRepo.findAll().get(0);
			return CardNumberParameterDTO.builder().cardFirstDigitLength(cardNumberParameter.getCardFirstDigitLength())
					.cardLastDigitLength(cardNumberParameter.getCardLastDigitLength())
					.entryTimeOut(cardNumberParameter.getEntryTimeOut()).scanOption(cardNumberParameter.getScanOption())
					.inputRetryLimit(cardNumberParameter.getInputRetryLimit())
					.sessionExpireTime(cardNumberParameter.getSessionExpireTime()).build();
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}
	}

	@Override
	public CardNumberParameterDTO updateCardNumberParameter(CardNumberParameterDTO cardNumberParameterDTO) {
		try {
			CardNumberParameter cardNumberParameter = cardNumberParameterRepo.findAll().get(0);
			if (cardNumberParameterDTO.getCardFirstDigitLength() != 0) {
				cardNumberParameter.setCardFirstDigitLength(cardNumberParameterDTO.getCardFirstDigitLength());
			}
			if (cardNumberParameterDTO.getCardLastDigitLength() != 0) {
				cardNumberParameter.setCardLastDigitLength(cardNumberParameterDTO.getCardLastDigitLength());
			}
			if (cardNumberParameterDTO.getEntryTimeOut() != 0) {
				cardNumberParameter.setEntryTimeOut(cardNumberParameterDTO.getEntryTimeOut());
			}
			if (cardNumberParameterDTO.getScanOption() != null) {
				cardNumberParameter.setScanOption(cardNumberParameterDTO.getScanOption());
			}
			if (cardNumberParameterDTO.getInputRetryLimit() != 0) {
				cardNumberParameter.setInputRetryLimit(cardNumberParameterDTO.getInputRetryLimit());
			}
			if (cardNumberParameterDTO.getSessionExpireTime() != 0) {
				cardNumberParameter.setSessionExpireTime(cardNumberParameterDTO.getSessionExpireTime());
			}
			cardNumberParameter = cardNumberParameterRepo.save(cardNumberParameter);
			return CardNumberParameterDTO.builder().cardFirstDigitLength(cardNumberParameter.getCardFirstDigitLength())
					.cardLastDigitLength(cardNumberParameter.getCardLastDigitLength())
					.entryTimeOut(cardNumberParameter.getEntryTimeOut()).scanOption(cardNumberParameter.getScanOption())
					.inputRetryLimit(cardNumberParameter.getInputRetryLimit())
					.sessionExpireTime(cardNumberParameter.getSessionExpireTime()).build();
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}
	}

	@Override
	public CardNumberParameterDTO createCardNumberParameter(CardNumberParameterDTO cardNumberParameterDTO) {
		if (cardNumberParameterRepo.findAll().size() > 0) {
			return null;
		} else {
			CardNumberParameter cardNumberParameter = CardNumberParameter.builder()
					.cardFirstDigitLength(cardNumberParameterDTO.getCardFirstDigitLength())
					.cardLastDigitLength(cardNumberParameterDTO.getCardLastDigitLength())
					.entryTimeOut(cardNumberParameterDTO.getEntryTimeOut())
					.scanOption(cardNumberParameterDTO.getScanOption())
					.inputRetryLimit(cardNumberParameterDTO.getInputRetryLimit())
					.sessionExpireTime(cardNumberParameterDTO.getSessionExpireTime()).build();
			cardNumberParameter = cardNumberParameterRepo.save(cardNumberParameter);
			return CardNumberParameterDTO.builder().cardFirstDigitLength(cardNumberParameter.getCardFirstDigitLength())
					.cardLastDigitLength(cardNumberParameter.getCardLastDigitLength())
					.entryTimeOut(cardNumberParameter.getEntryTimeOut()).scanOption(cardNumberParameter.getScanOption())
					.inputRetryLimit(cardNumberParameter.getInputRetryLimit())
					.sessionExpireTime(cardNumberParameter.getSessionExpireTime()).build();
		}

	}

	@Override
	public CardNumberParameterDTO deleteCardNumberParameter() {
		try {
			CardNumberParameter cardNumberParameter = cardNumberParameterRepo.findAll().get(0);
			return CardNumberParameterDTO.builder().cardFirstDigitLength(cardNumberParameter.getCardFirstDigitLength())
					.cardLastDigitLength(cardNumberParameter.getCardLastDigitLength())
					.entryTimeOut(cardNumberParameter.getEntryTimeOut()).scanOption(cardNumberParameter.getScanOption())
					.inputRetryLimit(cardNumberParameter.getInputRetryLimit())
					.sessionExpireTime(cardNumberParameter.getSessionExpireTime()).build();			
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}
	}

}
