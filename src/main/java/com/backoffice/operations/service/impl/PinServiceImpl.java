package com.backoffice.operations.service.impl;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.backoffice.operations.entity.CardEntity;
import com.backoffice.operations.entity.PinRequestEntity;
import com.backoffice.operations.entity.User;
import com.backoffice.operations.payloads.GetPinDTO;
import com.backoffice.operations.payloads.PinRequestDTO;
import com.backoffice.operations.payloads.PinResponseDTO;
import com.backoffice.operations.repository.CardRepository;
import com.backoffice.operations.repository.PinRequestRepository;
import com.backoffice.operations.repository.UserRepository;
import com.backoffice.operations.security.JwtTokenProvider;
import com.backoffice.operations.service.PinService;

@Service
public class PinServiceImpl implements PinService {

	@Autowired
	private PinRequestRepository pinRequestRepository;

	@Value("${external.api.pinUrl}")
	private String externalApiPinUrl;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CardRepository cardRepository;

	@Override
	public boolean storeAndSetPin(GetPinDTO pinRequestDTO, String token) {
		String userEmail = jwtTokenProvider.getUsername(token);
		Optional<User> user = userRepository.findByEmail(userEmail);
		if (user.isPresent()) {
			CardEntity cardEntity = cardRepository.findByUniqueKeyCivilId(pinRequestDTO.getUniqueKey());
			if (Objects.nonNull(cardEntity)) {
				PinRequestEntity pinRequestEntity = pinRequestRepository
						.findByUniqueKeyCivilId(cardEntity.getUniqueKeyCivilId());
				if (Objects.isNull(pinRequestEntity)) {
					pinRequestEntity = new PinRequestEntity();
					pinRequestEntity.setUniqueKeyCivilId(cardEntity.getUniqueKeyCivilId());
					pinRequestEntity.setCivilId(cardEntity.getCivilId());
					pinRequestEntity.setPin(pinRequestDTO.getPin());
					pinRequestEntity.setKitNo(cardEntity.getCardKitNo());
					pinRequestEntity.setExpiryDate(cardEntity.getExpiry());
					pinRequestEntity.setDob(cardEntity.getDobOfUser());
					pinRequestRepository.save(pinRequestEntity);
				}

				PinRequestDTO pinRequest = new PinRequestDTO();
				pinRequest.setEntityId(pinRequestEntity.getCivilId());
				pinRequest.setDob(pinRequestEntity.getDob());
				pinRequest.setExpiryDate(pinRequestEntity.getExpiryDate());
				pinRequest.setKitNo(pinRequestEntity.getKitNo());
				pinRequest.setPin(pinRequestEntity.getPin());

				HttpHeaders headers = new HttpHeaders();
				headers.add("TENANT", "ALIZZ_UAT");
				headers.setContentType(MediaType.APPLICATION_JSON);

				HttpEntity<PinRequestDTO> requestEntity = new HttpEntity<>(pinRequest, headers);
				ResponseEntity<PinResponseDTO> response = restTemplate.postForEntity(externalApiPinUrl, requestEntity,
						PinResponseDTO.class);
				if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null
						&& response.getBody().getResult() != null && response.getBody().getResult().isStatus()) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

}
