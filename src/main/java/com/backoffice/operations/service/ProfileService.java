package com.backoffice.operations.service;

import com.backoffice.operations.payloads.common.GenericResponseDTO;

public interface ProfileService {
    GenericResponseDTO<Object> getCustomerInfo(String uniqueKey, String nId, String lang, String token);
}
