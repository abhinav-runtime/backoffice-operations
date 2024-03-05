package com.backoffice.operations.service;

import com.backoffice.operations.payloads.common.GenericResponseDTO;

public interface BoCarddetailService {
	GenericResponseDTO<Object> fetchCardDeatils(String custNo);
}
