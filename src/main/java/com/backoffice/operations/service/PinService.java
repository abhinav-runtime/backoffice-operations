package com.backoffice.operations.service;

import com.backoffice.operations.payloads.GetPinDTO;

public interface PinService {
	
	public boolean storeAndSetPin(GetPinDTO pinRequestDTO);

}
