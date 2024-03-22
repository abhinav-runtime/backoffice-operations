package com.backoffice.operations.controller;

import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.TransferLimitService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transferLimit")
public class TransferLimitController {

    private final TransferLimitService transferLimitService;

    public TransferLimitController(TransferLimitService transferLimitService) {
        this.transferLimitService = transferLimitService;
    }

    @GetMapping
    public ResponseEntity<GenericResponseDTO<Object>> getTransferLimit(@RequestParam String customerType,
                                                                       @RequestParam String uniqueKey,
                                                                       @RequestParam String transactionType,
                                                                       @RequestParam Double transactionAmount,
                                                                       @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return new ResponseEntity<>(transferLimitService.getTransferLimit(customerType, uniqueKey, transactionType,
                transactionAmount), HttpStatus.OK);

    }
}
