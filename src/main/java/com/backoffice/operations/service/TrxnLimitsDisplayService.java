package com.backoffice.operations.service;

import com.backoffice.operations.payloads.TexnLimitsDisplayDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;

public interface TrxnLimitsDisplayService {
	GenericResponseDTO<Object> getTransferLimit(TexnLimitsDisplayDto.ResquestDTO requestDTO);
}
