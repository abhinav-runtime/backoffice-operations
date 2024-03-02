package com.backoffice.operations.service;

import com.backoffice.operations.payloads.common.GenericResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface PurposeService {
    GenericResponseDTO<Object> getPurposeList();
}
