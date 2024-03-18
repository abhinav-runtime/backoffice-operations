package com.backoffice.operations.controller;

import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.BeneficiaryBankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/api/v1/beneficiaryBank")
public class BeneficiaryBankController {

    private final BeneficiaryBankService beneficiaryBankService;

    public BeneficiaryBankController(BeneficiaryBankService beneficiaryBankService) {
        this.beneficiaryBankService = beneficiaryBankService;
    }

    @GetMapping()
    public ResponseEntity<GenericResponseDTO<Object>> getBeneficiaryBankList(@RequestHeader(HttpHeaders.AUTHORIZATION)  String token){
        return ResponseEntity.ok(beneficiaryBankService.getBeneficiaryBankList());
    }


}
