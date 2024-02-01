package com.backoffice.operations.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.exceptions.MaxResendAttemptsException;
import com.backoffice.operations.exceptions.OtpValidationException;
import com.backoffice.operations.payloads.BlockUnblockActionDTO;
import com.backoffice.operations.payloads.EntityIdDTO;
import com.backoffice.operations.payloads.ExternalApiResponseDTO;
import com.backoffice.operations.payloads.GetPinDTO;
import com.backoffice.operations.payloads.OtpRequestDTO;
import com.backoffice.operations.payloads.SecuritySettingsDTO;
import com.backoffice.operations.service.CivilIdService;
import com.backoffice.operations.service.OtpService;
import com.backoffice.operations.service.PinService;

@RestController
@RequestMapping("/api/otp")
public class OtpController {
	
	@Autowired
    private OtpService otpService;
	@Autowired
	private CivilIdService civilIdService;
	@Autowired
    private PinService pinService;

    @PostMapping("/validate")
    public ResponseEntity<String> validateOtp(@RequestBody OtpRequestDTO otpRequest) {
        try {
            otpService.validateOtp(otpRequest.getOtp());
            return ResponseEntity.ok("OTP validation successful");
        } catch (OtpValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<String> resendOtp(@RequestParam String userId) {
        try {
            otpService.resendOtp(userId);
            return ResponseEntity.ok("OTP resent successfully");
        } catch (MaxResendAttemptsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @GetMapping("/civil/{civilId}")
    public ResponseEntity<String> getEntityId(@PathVariable @Validated String civilId) {
        Optional<String> entityId = civilIdService.getEntityId(civilId);
        return entityId.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping("/card/getCardList")
    public ResponseEntity<ExternalApiResponseDTO> getCardList(@RequestBody @Validated EntityIdDTO entityIdDTO) {
        ExternalApiResponseDTO responseDTO = civilIdService.getCardList(entityIdDTO.getEntityId());
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/pin/storeAndSetPin")
    public ResponseEntity<String> storeAndSetPin(@RequestBody GetPinDTO pinRequestDTO) {
        if (pinService.storeAndSetPin(pinRequestDTO)) {
            return ResponseEntity.ok("Pin stored and set successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error storing or setting pin");
        }
    }
    
    @PostMapping("/card/fetchAllCustomerData")
    public ResponseEntity<Object> fetchAllCustomerData(@RequestBody @Validated EntityIdDTO entityIdDTO) {
    	Object responseDTO = civilIdService.fetchAllCustomerData(entityIdDTO.getEntityId());
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/card/blockUnblockCard")
    public ResponseEntity<Object> blockUnblockCard(@RequestBody @Validated BlockUnblockActionDTO blockUnblockCard) {
    	Object responseDTO = civilIdService.blockUnblockCard(blockUnblockCard);
        return ResponseEntity.ok(responseDTO);
    }
    
    @PostMapping("/saveSettings")
    public ResponseEntity<String> updateSecuritySettings(@RequestBody SecuritySettingsDTO securitySettingsDTO) {
        otpService.saveSecuritySettings(securitySettingsDTO);
        return new ResponseEntity<>("Security settings updated successfully", HttpStatus.OK);
    }
    
}
