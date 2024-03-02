package com.backoffice.operations.controller;

import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.PurposeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/purpose")
public class PurposeController {

    private final PurposeService purposeService;

    public PurposeController(PurposeService purposeService) {
        this.purposeService = purposeService;
    }

    @GetMapping()
    public ResponseEntity<GenericResponseDTO<Object>> getPurposeList(){
        return ResponseEntity.ok(purposeService.getPurposeList());
    }
}
