package com.backoffice.operations.service;

import java.util.Optional;

import com.backoffice.operations.payloads.AllCustomerResponseDTO;
import com.backoffice.operations.payloads.BlockUnblockActionDTO;
import com.backoffice.operations.payloads.ExternalApiResponseDTO;

public interface CivilIdService {
		
	Optional<String> getEntityId(String civilId);

	ExternalApiResponseDTO getCardList(String entityId);

	Object fetchAllCustomerData(String entityId);

	Object blockUnblockCard(BlockUnblockActionDTO blockUnblockCard);

}
