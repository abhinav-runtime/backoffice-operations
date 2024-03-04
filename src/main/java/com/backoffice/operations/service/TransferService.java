package com.backoffice.operations.service;

import com.backoffice.operations.payloads.SelfTransferDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;

public interface TransferService {

    GenericResponseDTO<Object> transferToBank(SelfTransferDTO selfTransferDTO);
}
