package com.backoffice.operations.controller;

import com.backoffice.operations.payloads.TransferRequestDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.TransferService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/self")
    public ResponseEntity<GenericResponseDTO<Object>> transferToBank(@RequestBody TransferRequestDto transferRequest) throws JsonProcessingException {
        return ResponseEntity.ok(transferService.transferToBank(transferRequest));
    }
}
