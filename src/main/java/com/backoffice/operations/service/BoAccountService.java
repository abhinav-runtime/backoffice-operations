package com.backoffice.operations.service;

import com.backoffice.operations.payloads.common.GenericResponseDTO;

public interface BoAccountService {
	GenericResponseDTO<Object> getAccountDetails(String custNo);
}
