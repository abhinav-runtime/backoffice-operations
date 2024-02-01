package com.backoffice.operations.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.backoffice.operations.entity.PinRequestEntity;
import com.backoffice.operations.payloads.GetPinDTO;
import com.backoffice.operations.payloads.PinResponseDTO;
import com.backoffice.operations.repository.PinRequestRepository;
import com.backoffice.operations.service.PinService;

@Service
public class PinServiceImpl implements PinService {
	
	@Autowired
    private PinRequestRepository pinRequestRepository;
	
	@Value("${external.api.pinUrl}")
    private String externalApiPinUrl;
    
    @Autowired
    private RestTemplate restTemplate; 

    public boolean storeAndSetPin(GetPinDTO pinRequestDTO) {
        // Store data in the database
        PinRequestEntity pinRequestEntity = new PinRequestEntity();
        pinRequestEntity.setEntityId(pinRequestDTO.getEntityId());
        pinRequestEntity.setPin(pinRequestDTO.getPin());
        pinRequestEntity.setKitNo(pinRequestDTO.getKitNo());
        pinRequestEntity.setExpiryDate(pinRequestDTO.getExpiryDate());
        pinRequestEntity.setDob(pinRequestDTO.getDob());
        pinRequestRepository.save(pinRequestEntity);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("TENANT", "ALIZZ_UAT");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GetPinDTO> requestEntity = new HttpEntity<>(pinRequestDTO, headers);
        ResponseEntity<PinResponseDTO> response = restTemplate.postForEntity(
        		externalApiPinUrl,
                requestEntity,
                PinResponseDTO.class
        );
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().getResult() != null
                && response.getBody().getResult().isStatus()) {
            return true;
        } else {
            return false;
        }
    }

}
