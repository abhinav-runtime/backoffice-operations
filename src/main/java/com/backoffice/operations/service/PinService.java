package com.backoffice.operations.service;

import com.backoffice.operations.payloads.GetPinDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;

public interface PinService {
	
	GenericResponseDTO<Object> storeAndSetPin(GetPinDTO pinRequestDTO, String token);

}
