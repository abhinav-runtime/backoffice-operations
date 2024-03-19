package com.backoffice.operations.controller;

import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.TransferLimitService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transferLimit")
public class TransferLimitController {

    private final TransferLimitService transferLimitService;

    public TransferLimitController(TransferLimitService transferLimitService) {
        this.transferLimitService = transferLimitService;
    }

    @GetMapping("/calculateFee")
    public ResponseEntity<GenericResponseDTO<Object>> getTransferLimit(@RequestParam String customerType,
                                                                       @RequestParam String uniqueKey,
                                                                       @RequestParam String transactionType,
                                                                       @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        transferLimitService.getTransferLimit(customerType,uniqueKey,transactionType);

        return null;
    }
}
