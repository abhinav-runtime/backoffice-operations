package com.backoffice.operations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.exceptions.InvalidPasscodeException;
import com.backoffice.operations.exceptions.PasscodeLockedException;
import com.backoffice.operations.payloads.PasscodeDto;
import com.backoffice.operations.service.PasscodeService;

@RestController
@RequestMapping("/api/passcode")
public class PasscodeController {

    @Autowired
    private PasscodeService passcodeService;

    @PostMapping("/validate")
    public ResponseEntity<String> validatePasscode(@RequestBody PasscodeDto passcodeDto) {
        try {
            if (passcodeService.validatePasscode(passcodeDto)) {
                return ResponseEntity.ok("Passcode validated successfully.");
            }
        } catch (PasscodeLockedException | InvalidPasscodeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.badRequest().body("Passcode validation failed.");
    }
}
