package com.backoffice.operations.service;

import com.backoffice.operations.payloads.AlizzTransferDto;
import com.backoffice.operations.payloads.AlizzTransferRequestDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AlizzTransferService {
    GenericResponseDTO<Object> transferToAlizzAccount(AlizzTransferRequestDto alizzTransferRequestDto) throws JsonProcessingException;

    public Double calculateFee() throws JsonProcessingException;

}
