package com.backoffice.operations.service;

import com.backoffice.operations.payloads.AlizzTransferRequestDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;

public interface ACHService {

    GenericResponseDTO<Object> transferToACHAccount(AlizzTransferRequestDto alizzTransferRequestDto);
}
