package com.backoffice.operations.controller;

import com.backoffice.operations.payloads.CivilIdAPIResponse;
import com.backoffice.operations.payloads.UpdateProfileRequest;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/profile")
    public ResponseEntity<GenericResponseDTO<Object>> getCustomerInfoByNid(@RequestParam String uniqueKey,
                                                                           @RequestParam String nId,
                                                                           @RequestParam String lang,
                                                                           @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        GenericResponseDTO<Object> genericResponseDTO = profileService.getCustomerInfo(uniqueKey, nId, lang, token.substring("Bearer ".length()));
        return ResponseEntity.ok(genericResponseDTO);
    }

    @PutMapping("/update/{uniqueKey}")
    public ResponseEntity<GenericResponseDTO<Object>> updateProfile(@PathVariable String uniqueKey, @RequestBody UpdateProfileRequest updateProfileRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        GenericResponseDTO<Object> genericResponseDTO = profileService.updateProfile(uniqueKey, updateProfileRequest, token.substring("Bearer ".length()));
        return ResponseEntity.ok(genericResponseDTO);
    }

}
