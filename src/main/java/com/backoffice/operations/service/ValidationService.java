package com.backoffice.operations.service;

import com.backoffice.operations.payloads.common.GenericResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface ValidationService {
    GenericResponseDTO<Object> validateUserAccount(String accountNumber);
}
