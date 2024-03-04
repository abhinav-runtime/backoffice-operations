package com.backoffice.operations.service;

import com.backoffice.operations.payloads.TransactionLimitsDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;


public interface TransactionLimitService {
	
	GenericResponseDTO<Object> setMerchantOutletLimits(TransactionLimitsDTO transactionLimitsDTO, String token);
	GenericResponseDTO<Object> setOnlineShoppingLimits(TransactionLimitsDTO.OnlineShopping onlineShoppingLimitDTO, String token);
	GenericResponseDTO<Object> setAtmWithdrawalLimits(TransactionLimitsDTO.ATMwithdrawal atmWithdrawalLimitDTO, String token);
	GenericResponseDTO<Object> setTapAndPayLimits(TransactionLimitsDTO.TapAndPay tapAndPayLimitDTO, String token);
	
	GenericResponseDTO<Object> getAllTransactionLimits(String token);	
	GenericResponseDTO<Object> getTransactionLimitsByCustId(String customerId, String token);
}
