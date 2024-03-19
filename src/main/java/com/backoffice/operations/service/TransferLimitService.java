package com.backoffice.operations.service;

import com.backoffice.operations.payloads.common.GenericResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface TransferLimitService {
    GenericResponseDTO<Object> getTransferLimit(String customerType, String uniqueKey, String transactionType);
}
