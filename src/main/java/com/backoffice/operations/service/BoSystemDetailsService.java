package com.backoffice.operations.service;

import java.util.List;

import com.backoffice.operations.payloads.BoSystemDetailsResponseDTO;

public interface BoSystemDetailsService {
	List<BoSystemDetailsResponseDTO> getSystemDetails(String custNo);
}
