package com.backoffice.operations.service;

import com.backoffice.operations.payloads.BlockUnblockActionDTO;
import com.backoffice.operations.payloads.EntityIdDTO;
import com.backoffice.operations.payloads.ValidationResultDTO;

public interface CivilIdService {

	ValidationResultDTO validateCivilId(String entityId, String token);

	ValidationResultDTO verifyCard(EntityIdDTO entityIdDTO, String token);

	Object fetchAllCustomerData(EntityIdDTO entityIdDTO, String token);

	Object blockUnblockCard(BlockUnblockActionDTO blockUnblockCard);

}
