package com.backoffice.operations.service;

import com.backoffice.operations.payloads.AlizzTransferRequestDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public interface ACHService {

    GenericResponseDTO<Object> transferToACHAccount(AlizzTransferRequestDto alizzTransferRequestDto) throws JsonProcessingException;
}
