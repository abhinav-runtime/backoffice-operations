package com.backoffice.operations.service;

import com.backoffice.operations.payloads.CardPinParameterDto;

public interface CardPinParameterService {
	CardPinParameterDto getCardPinParameter();

	CardPinParameterDto updateCardPinParameter(CardPinParameterDto cardPinParameterDto);

	CardPinParameterDto createCardPinParameter(CardPinParameterDto cardPinParameterDto);

	CardPinParameterDto deleteCardPinParameter();
}
