package com.backoffice.operations.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

import com.backoffice.operations.exceptions.InvalidPasscodeException;
import com.backoffice.operations.exceptions.PasscodeLockedException;
import com.backoffice.operations.payloads.PasscodeDto;

@Service
public class PasscodeService {

    private static final int MAX_INVALID_ATTEMPTS = 5;
    private static final int PASSCODE_LENGTH = 6;
    private static final int PASSCODE_DURATION_MINUTES = 5;

    private int invalidAttempts = 0;
    private LocalDateTime lastPasscodeEntryTime;

    public boolean validatePasscode(PasscodeDto passcodeDto) {
        if (isPasscodeLocked()) {
            throw new PasscodeLockedException("Passcode entry is locked. Try again after some time.");
        }

        int enteredPasscode = passcodeDto.getPasscode();

        // Add validation for passcode length and type
        if (String.valueOf(enteredPasscode).length() != PASSCODE_LENGTH) {
            throw new InvalidPasscodeException("Passcode length should be 6 digits.");
        }

        // Add validation for passcode type (integer)
        try {
            Integer.parseInt(String.valueOf(enteredPasscode));
        } catch (NumberFormatException e) {
            throw new InvalidPasscodeException("Passcode should be an integer.");
        }

        // Additional business logic for passcode validation

        // If passcode is valid, reset invalid attempts and update last entry time
        invalidAttempts = 0;
        lastPasscodeEntryTime = LocalDateTime.now();

        // Add logic for successful passcode validation
        return true;
    }

    private boolean isPasscodeLocked() {
        if (invalidAttempts >= MAX_INVALID_ATTEMPTS) {
            // Check if the duration since the last passcode entry is less than PASSCODE_DURATION_MINUTES
            if (lastPasscodeEntryTime != null &&
                    lastPasscodeEntryTime.plusMinutes(PASSCODE_DURATION_MINUTES).isAfter(LocalDateTime.now())) {
                return true;
            }
        }
        return false;
    }
}
