package com.backoffice.operations.service;

import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;

public interface BoCarddetailService {
	GenericResponseDTO<Object> fetchCardDeatils(String custNo);
	GenericResponseDTO<Object> fetchPreference(String custNo);
	GenericResponseDTO<Object> setPreference(JsonNode preference);
}
