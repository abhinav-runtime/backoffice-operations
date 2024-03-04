package com.backoffice.operations.controller;

import com.backoffice.operations.payloads.SelfTransferDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/self")
    public ResponseEntity<GenericResponseDTO<Object>> transferToBank(@RequestBody SelfTransferDTO selfTransferDTO) {
        return ResponseEntity.ok(transferService.transferToBank(selfTransferDTO));
    }
}
