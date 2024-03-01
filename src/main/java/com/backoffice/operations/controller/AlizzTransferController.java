package com.backoffice.operations.controller;

import com.backoffice.operations.payloads.AlizzTransferDto;
import com.backoffice.operations.payloads.AlizzTransferRequestDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.AlizzTransferService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/allizTransfer")
public class AlizzTransferController {

    private final AlizzTransferService alizzTransferService;

    public AlizzTransferController(AlizzTransferService alizzTransferService) {
        this.alizzTransferService = alizzTransferService;
    }

    @PostMapping
    public ResponseEntity<GenericResponseDTO<Object>> transferToAlizzAccount(@RequestBody AlizzTransferRequestDto alizzTransferRequestDto) throws JsonProcessingException {
        return ResponseEntity.ok(alizzTransferService.transferToAlizzAccount(alizzTransferRequestDto));
    }
}
