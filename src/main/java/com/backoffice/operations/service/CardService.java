package com.backoffice.operations.service;

import com.backoffice.operations.payloads.CardStatusDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;

public interface CardService {
	GenericResponseDTO<Object> getCardStatus(String uniqueKey);
	GenericResponseDTO<Object> tempBlockAndUnblock(String uniqueKey, CardStatusDto.requestDto requestDto);
}
