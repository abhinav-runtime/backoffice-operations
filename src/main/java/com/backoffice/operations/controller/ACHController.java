package com.backoffice.operations.controller;

import com.backoffice.operations.payloads.AlizzTransferRequestDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.ACHService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/achTransfer")
public class ACHController {

    private ACHService achService;

    @PostMapping
    public ResponseEntity<GenericResponseDTO<Object>> transferToACHAccount(
            @RequestBody AlizzTransferRequestDto alizzTransferRequestDto) throws JsonProcessingException {
        return ResponseEntity.ok(achService.transferToACHAccount(alizzTransferRequestDto));
    }

}
