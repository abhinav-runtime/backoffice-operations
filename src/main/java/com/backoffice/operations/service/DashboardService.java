package com.backoffice.operations.service;

import com.backoffice.operations.payloads.EditInfoRequestDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;

import org.springframework.stereotype.Service;

@Service
public interface DashboardService {
	GenericResponseDTO<Object> getDashboardDetails(String uniqueKey);

	GenericResponseDTO<Object> getDashboardInfo(String uniqueKey);

	GenericResponseDTO<Object> getAccountTransactions(String accountNumber, String fromDate, String toDate,String uniqueKey);

	GenericResponseDTO<Object> getCreditCardTransactions(String fromDate, String toDate, String uniqueKey);

	GenericResponseDTO<Object> getUpComingBills(String uniqueKey);

	GenericResponseDTO<Object> editInfo(EditInfoRequestDto editInfoRequestDto);
}
