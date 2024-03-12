package com.backoffice.operations.service;

import com.backoffice.operations.payloads.CardNumberParameterDTO;

public interface CardNumberParameterService {
	public CardNumberParameterDTO getCardNumberParameter();
	public CardNumberParameterDTO updateCardNumberParameter(CardNumberParameterDTO cardNumberParameterDTO);
	public CardNumberParameterDTO createCardNumberParameter(CardNumberParameterDTO cardNumberParameterDTO);
}
