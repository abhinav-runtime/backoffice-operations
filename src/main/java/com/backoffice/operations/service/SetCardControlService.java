package com.backoffice.operations.service;

import com.backoffice.operations.payloads.SetCardControlsDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;

public interface SetCardControlService {
	
	GenericResponseDTO<Object> setControls(SetCardControlsDTO setCardControlsDTO, String token);
	GenericResponseDTO<Object> getCardControlsLists(String token);
	
}
