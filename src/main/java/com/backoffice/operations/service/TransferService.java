package com.backoffice.operations.service;

import com.backoffice.operations.payloads.SelfTransferDTO;
import com.backoffice.operations.payloads.TransferRequestDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Optional;

public interface TransferService {

    GenericResponseDTO<Object> transferToBank(SelfTransferDTO selfTransferDTO) throws JsonProcessingException;
}
