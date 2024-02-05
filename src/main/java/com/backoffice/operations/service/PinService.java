package com.backoffice.operations.service;

import com.backoffice.operations.payloads.GetPinDTO;
import com.backoffice.operations.payloads.ValidationResultDTO;

public interface PinService {
	
	ValidationResultDTO storeAndSetPin(GetPinDTO pinRequestDTO, String token);

}
