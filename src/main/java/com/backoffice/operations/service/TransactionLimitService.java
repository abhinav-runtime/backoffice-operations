package com.backoffice.operations.service;

import com.backoffice.operations.payloads.CardSettingDto;
import com.backoffice.operations.payloads.TransactionLimitsDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;


public interface TransactionLimitService {
	
	GenericResponseDTO<Object> setTransactionLimits(TransactionLimitsDTO transactionLimitsDTO);

	GenericResponseDTO<Object> getAllTransactionLimits(String token);	
	GenericResponseDTO<Object> getTransactionLimitsByCustId(String uniqueKey);
	GenericResponseDTO<Object> getCardSetting(String uniqueKey);
	GenericResponseDTO<Object> setCardSetting(CardSettingDto cardSettingDto, String uniqueKey);
}
