package com.backoffice.operations.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.CardPinParameter;
import com.backoffice.operations.payloads.CardPinParameterDto;
import com.backoffice.operations.repository.CardPinParameterRepository;
import com.backoffice.operations.service.CardPinParameterService;

@Service
public class CardPinParameterServiceImp implements CardPinParameterService {
	private static final Logger logger = LoggerFactory.getLogger(CardPinParameterServiceImp.class);
	@Autowired
	private CardPinParameterRepository cardPinParameterRepository;

	@Override
	public CardPinParameterDto getCardPinParameter() {
		try {
			CardPinParameter cardPinParameter = cardPinParameterRepository.findAll().get(0);
			return CardPinParameterDto.builder().cardPinMaximumAttempts(cardPinParameter.getCardPinMaximumAttempts())
					.cardPinCooldownInSec(cardPinParameter.getCardPinCooldownInSec())
					.incorrectPinAttempts(cardPinParameter.getIncorrectPinAttempts())
					.pinHistoryDepth(cardPinParameter.getPinHistoryDepth()).pinLength(cardPinParameter.getPinLength())
					.repetitiveDigits(cardPinParameter.getRepetitiveDigits())
					.sequentialDigits(cardPinParameter.getSequentialDigits())
					.sessionExpiry(cardPinParameter.getSessionExpiry()).build();
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}
	}

	@Override
	public CardPinParameterDto updateCardPinParameter(CardPinParameterDto cardPinParameterDto) {
		try {
			CardPinParameter cardPinParameter = cardPinParameterRepository.findAll().get(0);
			if (cardPinParameterDto.getCardPinMaximumAttempts() != 0) {
				cardPinParameter.setCardPinMaximumAttempts(cardPinParameterDto.getCardPinMaximumAttempts());
			}
			if (cardPinParameterDto.getCardPinCooldownInSec() != 0) {
				cardPinParameter.setCardPinCooldownInSec(cardPinParameterDto.getCardPinCooldownInSec());
			}
			if (cardPinParameterDto.getIncorrectPinAttempts() != 0) {
				cardPinParameter.setIncorrectPinAttempts(cardPinParameterDto.getIncorrectPinAttempts());
			}
			if (cardPinParameterDto.getPinHistoryDepth() != 0) {
				cardPinParameter.setPinHistoryDepth(cardPinParameterDto.getPinHistoryDepth());
			}
			if (cardPinParameterDto.getPinLength() != 0) {
				cardPinParameter.setPinLength(cardPinParameterDto.getPinLength());
			}
			if (cardPinParameterDto.getRepetitiveDigits() != 0) {
				cardPinParameter.setRepetitiveDigits(cardPinParameterDto.getRepetitiveDigits());
			}
			if (cardPinParameterDto.getSequentialDigits() != 0) {
				cardPinParameter.setSequentialDigits(cardPinParameterDto.getSequentialDigits());
			}
			if (cardPinParameterDto.getSessionExpiry() != 0) {
				cardPinParameter.setSessionExpiry(cardPinParameterDto.getSessionExpiry());
			}
			cardPinParameter = cardPinParameterRepository.save(cardPinParameter);
			return CardPinParameterDto.builder().cardPinMaximumAttempts(cardPinParameter.getCardPinMaximumAttempts())
					.cardPinCooldownInSec(cardPinParameter.getCardPinCooldownInSec())
					.incorrectPinAttempts(cardPinParameter.getIncorrectPinAttempts())
					.pinHistoryDepth(cardPinParameter.getPinHistoryDepth()).pinLength(cardPinParameter.getPinLength())
					.repetitiveDigits(cardPinParameter.getRepetitiveDigits())
					.sequentialDigits(cardPinParameter.getSequentialDigits())
					.sessionExpiry(cardPinParameter.getSessionExpiry()).build();
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}

	}

	@Override
	public CardPinParameterDto createCardPinParameter(CardPinParameterDto cardPinParameterDto) {
		try {
			List<CardPinParameter> cardPinParameterList = cardPinParameterRepository.findAll();
			if (cardPinParameterList.size() == 0) {
				CardPinParameter cardPinParameter = CardPinParameter.builder()
						.cardPinMaximumAttempts(cardPinParameterDto.getCardPinMaximumAttempts())
						.cardPinCooldownInSec(cardPinParameterDto.getCardPinCooldownInSec())
						.incorrectPinAttempts(cardPinParameterDto.getIncorrectPinAttempts())
						.pinHistoryDepth(cardPinParameterDto.getPinHistoryDepth())
						.pinLength(cardPinParameterDto.getPinLength())
						.repetitiveDigits(cardPinParameterDto.getRepetitiveDigits())
						.sequentialDigits(cardPinParameterDto.getSequentialDigits())
						.sessionExpiry(cardPinParameterDto.getSessionExpiry()).build();
				cardPinParameter = cardPinParameterRepository.save(cardPinParameter);
				return CardPinParameterDto.builder()
						.cardPinMaximumAttempts(cardPinParameter.getCardPinMaximumAttempts())
						.cardPinCooldownInSec(cardPinParameter.getCardPinCooldownInSec())
						.incorrectPinAttempts(cardPinParameter.getIncorrectPinAttempts())
						.pinHistoryDepth(cardPinParameter.getPinHistoryDepth())
						.pinLength(cardPinParameter.getPinLength())
						.repetitiveDigits(cardPinParameter.getRepetitiveDigits())
						.sequentialDigits(cardPinParameter.getSequentialDigits())
						.sessionExpiry(cardPinParameter.getSessionExpiry()).build();
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}
	}

	@Override
	public CardPinParameterDto deleteCardPinParameter() {
		try {
			CardPinParameter cardPinParameter = cardPinParameterRepository.findAll().get(0);
			cardPinParameterRepository.delete(cardPinParameter);
			return CardPinParameterDto.builder().cardPinMaximumAttempts(cardPinParameter.getCardPinMaximumAttempts())
					.cardPinCooldownInSec(cardPinParameter.getCardPinCooldownInSec())
					.incorrectPinAttempts(cardPinParameter.getIncorrectPinAttempts())
					.pinHistoryDepth(cardPinParameter.getPinHistoryDepth()).pinLength(cardPinParameter.getPinLength())
					.repetitiveDigits(cardPinParameter.getRepetitiveDigits())
					.sequentialDigits(cardPinParameter.getSequentialDigits())
					.sessionExpiry(cardPinParameter.getSessionExpiry()).build();
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			return null;
		}

	}

}
