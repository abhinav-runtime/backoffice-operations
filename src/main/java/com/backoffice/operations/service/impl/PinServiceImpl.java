package com.backoffice.operations.service.impl;

import java.util.HashMap;
import java.util.Map;
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
import com.backoffice.operations.payloads.common.GenericResponseDTO;
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
	public GenericResponseDTO<Object> storeAndSetPin(GetPinDTO pinRequestDTO, String token) {
		String userEmail = jwtTokenProvider.getUsername(token);
		Optional<User> user = userRepository.findByEmail(userEmail);
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
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
				String reversedExpiryDate = pinRequestEntity.getExpiryDate().substring(2)
						+ pinRequestEntity.getExpiryDate().substring(0, 2);
				pinRequest.setExpiryDate(reversedExpiryDate);
				pinRequest.setKitNo(pinRequestEntity.getKitNo());
				pinRequest.setPin(pinRequestEntity.getPin());

				HttpHeaders headers = new HttpHeaders();
				headers.add("TENANT", "ALIZZ_UAT");
				headers.setContentType(MediaType.APPLICATION_JSON);

				HttpEntity<PinRequestDTO> requestEntity = new HttpEntity<>(pinRequest, headers);
				ResponseEntity<PinResponseDTO> response = null;
				try {
					response = restTemplate.postForEntity(externalApiPinUrl, requestEntity, PinResponseDTO.class);
				} catch (Exception e) {
					Map<String, Object> data = new HashMap<>();
					responseDTO.setStatus("Success");
					responseDTO.setMessage("Success");
					data.put("uniqueKey", cardEntity.getUniqueKeyCivilId());
					data.put("dob", pinRequest.getDob());
					data.put("entityId", pinRequest.getEntityId());
					data.put("expiryDate", pinRequest.getExpiryDate());
					data.put("kitNo", pinRequest.getKitNo());

					responseDTO.setData(data);
					return responseDTO;
				}

				if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null
						&& response.getBody().getResult() != null && response.getBody().getResult().isStatus()) {
					Map<String, Object> data = new HashMap<>();
					responseDTO.setStatus("Success");
					responseDTO.setMessage("Success");
					data.put("uniqueKey", cardEntity.getUniqueKeyCivilId());
					data.put("dob", pinRequest.getDob());
					data.put("entityId", pinRequest.getEntityId());
					data.put("expiryDate", pinRequest.getExpiryDate());
					data.put("kitNo", pinRequest.getKitNo());
					responseDTO.setData(data);
					return responseDTO;
				} else {
					Map<String, Object> data = new HashMap<>();
					responseDTO.setStatus("Failure");
					responseDTO.setMessage("Something went wrong");
					data.put("uniqueKey", cardEntity.getUniqueKeyCivilId());
					responseDTO.setData(data);
					return responseDTO;
				}
			}
		}
		Map<String, Object> data = new HashMap<>();

		responseDTO.setStatus("Failure");
		responseDTO.setMessage("Something went wrong");
		responseDTO.setData(data);
		return responseDTO;
	}

}
