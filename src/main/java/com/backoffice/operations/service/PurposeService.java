package com.backoffice.operations.service;

import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.stereotype.Service;

@Service
public interface PurposeService {
    GenericResponseDTO<Object> getPurposeList();
	GenericResponseDTO<Object> getPurposeNetworkACH();
}
