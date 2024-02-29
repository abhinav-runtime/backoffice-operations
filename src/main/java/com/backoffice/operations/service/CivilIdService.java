package com.backoffice.operations.service;

import com.backoffice.operations.payloads.BlockUnblockActionDTO;
import com.backoffice.operations.payloads.EntityIdDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;

public interface CivilIdService {
		
	GenericResponseDTO<Object> validateCivilId(String entityId, String token);

	GenericResponseDTO<Object> verifyCard(EntityIdDTO entityIdDTO, String token);

	Object fetchAllCustomerData(EntityIdDTO entityIdDTO, String token);

	Object blockUnblockCard(BlockUnblockActionDTO blockUnblockCard);

}
