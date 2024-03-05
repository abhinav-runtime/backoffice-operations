package com.backoffice.operations.service;

import com.backoffice.operations.payloads.TransactionLimitsDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;


public interface TransactionLimitService {
	
	GenericResponseDTO<Object> setTransactionLimits(TransactionLimitsDTO transactionLimitsDTO, String token);

	GenericResponseDTO<Object> getAllTransactionLimits(String token);	
	GenericResponseDTO<Object> getTransactionLimitsByCustId(String uniqueKey, String token);
}
