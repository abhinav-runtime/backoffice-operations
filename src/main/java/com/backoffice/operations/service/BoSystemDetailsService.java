package com.backoffice.operations.service;

import java.util.List;

import com.backoffice.operations.payloads.BoSystemDetailsResponseDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;

public interface BoSystemDetailsService {
	List<BoSystemDetailsResponseDTO> getSystemDetails(String custNo);
	GenericResponseDTO<Object> getSystemLogs();
}
