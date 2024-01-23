package com.backoffice.operations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.exceptions.MaxResendAttemptsException;
import com.backoffice.operations.exceptions.OtpValidationException;
import com.backoffice.operations.payloads.OtpRequestDTO;
import com.backoffice.operations.service.OtpService;

@RestController
@RequestMapping("/api/otp")
public class OtpController {
	
	@Autowired
    private OtpService otpService;

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

}
