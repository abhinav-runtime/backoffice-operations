package com.backoffice.operations.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.backoffice.operations.entity.CardEntity;
import com.backoffice.operations.entity.SetCardControlsEntity;
import com.backoffice.operations.entity.User;
import com.backoffice.operations.payloads.SetCardControlsDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.CardRepository;
import com.backoffice.operations.repository.SetCardControlsRepository;
import com.backoffice.operations.repository.UserRepository;
import com.backoffice.operations.security.JwtTokenProvider;
import com.backoffice.operations.service.SetCardControlService;

@Service
public class SetCardControlServiceImpl implements SetCardControlService {
	
	@Value("${external.api.setPreferenceUrl}")
	private String setPreferenceUrl;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SetCardControlsRepository setCardControlsRepository;

	@Autowired
	private RestTemplate basicAuthRestTemplate;

	@Autowired
	private CardRepository cardRepository;

	@Override
	public GenericResponseDTO<Object> setControls(SetCardControlsDTO setCardControlsDTO, String token) {
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		String userEmail = jwtTokenProvider.getUsername(token);
		Optional<User> user = userRepository.findByEmail(userEmail);

		if (user.isPresent()) 
		{
			CardEntity cardEntity = cardRepository.findByUniqueKeyCivilId(setCardControlsDTO.getUniqueKey());

			SetCardControlsEntity setCardControlsEntity = new SetCardControlsEntity();
			SetCardControlsEntity newSetCardControlsEntity = setCardControlsRepository
					.findByUniqueKey(setCardControlsDTO.getUniqueKey());

			HttpHeaders headers = new HttpHeaders();
			headers.add("TENANT", "ALIZZ_UAT");
			headers.setContentType(MediaType.APPLICATION_JSON);
			String requestBody = "{\r\n    \"entityId\": \"" + cardEntity.getCivilId() + "\",\r\n    \"atm\": " + setCardControlsDTO.isAtmFlag()
					+ ",\r\n    \"ecom\": " + setCardControlsDTO.isEcomFlag() + ",\r\n    \"pos\": " + setCardControlsDTO.isPosFlag() + ",\r\n    \"contactless\": "
					+ setCardControlsDTO.isContactlessFlag() + "\r\n}";
			HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
			ResponseEntity<Object> responseEntity = basicAuthRestTemplate.exchange(setPreferenceUrl, HttpMethod.POST,
					requestEntity, Object.class);
			if ((responseEntity.getStatusCode() == HttpStatus.OK) && (responseEntity.getBody() != null)) {
				
				if (Objects.nonNull(newSetCardControlsEntity)) {
					newSetCardControlsEntity.setPosFlag(setCardControlsDTO.isPosFlag());
					newSetCardControlsEntity.setEcomFlag(setCardControlsDTO.isEcomFlag());
					newSetCardControlsEntity.setAtmFlag(setCardControlsDTO.isAtmFlag());
					newSetCardControlsEntity.setContactlessFlag(setCardControlsDTO.isContactlessFlag());
					newSetCardControlsEntity.setDateModified(LocalDateTime.now());
					
					setCardControlsRepository.save(newSetCardControlsEntity);
					Map<String, Object> data = new HashMap<>();
					responseDTO.setStatus("Success");
					responseDTO.setMessage("Card Controls are set!");
					data.put("Transaction limits", newSetCardControlsEntity);
					responseDTO.setData(data);
				} else {
					setCardControlsEntity.setUniqueKey(setCardControlsDTO.getUniqueKey());
					setCardControlsEntity.setPosFlag(setCardControlsDTO.isPosFlag());
					setCardControlsEntity.setEcomFlag(setCardControlsDTO.isEcomFlag());
					setCardControlsEntity.setAtmFlag(setCardControlsDTO.isAtmFlag());
					setCardControlsEntity.setContactlessFlag(setCardControlsDTO.isContactlessFlag());
					setCardControlsEntity.setDateModified(LocalDateTime.now());
					
					setCardControlsRepository.save(setCardControlsEntity);
					Map<String, Object> data = new HashMap<>();
					responseDTO.setStatus("Success");
					responseDTO.setMessage("Card Controls are set!");
					data.put("Transaction limits", setCardControlsEntity);
					responseDTO.setData(data);
				}
				return responseDTO;
			}
		}
		responseDTO.setStatus("Failure");
		responseDTO.setMessage("Something went wrong");
		return responseDTO;
	}
	
	@Override
	public GenericResponseDTO<Object> getCardControlsLists(String token) {
		
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		String userEmail = jwtTokenProvider.getUsername(token);
		Optional<User> user = userRepository.findByEmail(userEmail);

		if (user.isPresent()) {
			List<SetCardControlsEntity> setCardControlsEntity = setCardControlsRepository.findAll();
			Map<String, Object> data = new HashMap<>();
			responseDTO.setStatus("Success");
			responseDTO.setMessage("Card Controls list!");
			data.put("list", setCardControlsEntity);
			responseDTO.setData(data);
			return responseDTO;
		}
		
		responseDTO.setStatus("Failure");
		responseDTO.setMessage("Something went wrong");
		return responseDTO;
	}
}
