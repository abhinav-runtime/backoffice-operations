package com.backoffice.operations.service;

import com.backoffice.operations.payloads.CivilIdAPIResponse;
import com.backoffice.operations.payloads.UpdateProfileRequest;
import com.backoffice.operations.payloads.common.GenericResponseDTO;

public interface ProfileService {
    GenericResponseDTO<Object> getCustomerInfo(String uniqueKey, String nId, String lang, String token);

    GenericResponseDTO<Object> updateProfile(String uniqueKey, UpdateProfileRequest updateProfileRequest, String token);

    String verifyEmail(String token);
}
