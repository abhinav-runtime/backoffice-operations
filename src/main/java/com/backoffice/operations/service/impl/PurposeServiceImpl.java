package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.Purpose;
import com.backoffice.operations.payloads.AccessTokenResponse;
import com.backoffice.operations.payloads.PurposeResponseDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.PurposeRepository;
import com.backoffice.operations.service.PurposeService;
import com.backoffice.operations.utils.CommonUtils;
import com.fasterxml.jackson.databind.JsonNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class PurposeServiceImpl implements PurposeService {
	private static final Logger logger = LoggerFactory.getLogger(PurposeServiceImpl.class);
	private final PurposeRepository purposeRepository;

	public PurposeServiceImpl(PurposeRepository purposeRepository) {
		this.purposeRepository = purposeRepository;
	}

	@Value("${external.api.purpose.network.ach}")
	private String apiUrl;

	@Autowired
	private CommonUtils commonUtils;

	@Autowired
	@Qualifier("jwtAuth")
	private RestTemplate jwtAuthRestTemplate;

	@Override
	public GenericResponseDTO<Object> getPurposeList() {
		List<Purpose> purposeList = purposeRepository.findAll();

		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		Map<String, Object> data = new HashMap<>();
		data.put("purposeList", purposeList);
		responseDTO.setStatus("Success");
		responseDTO.setMessage("Success");
		responseDTO.setData(data);
		return responseDTO;
	}

	@Override
	public GenericResponseDTO<Object> getPurposeNetworkACH() {
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		String accessToken = null;

		try {
			List<Object> response = new ArrayList<>();
			ResponseEntity<AccessTokenResponse> responseForAccessToken = commonUtils.getToken();
			logger.info("Response For Access Token: {}", responseForAccessToken.getBody());

			accessToken = Objects.requireNonNull(responseForAccessToken.getBody().getAccessToken());
			logger.info("Authorization: {}", accessToken);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(accessToken);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<>(headers);
			ResponseEntity<JsonNode> responseEntity = jwtAuthRestTemplate.exchange(apiUrl, HttpMethod.GET,
					requestEntity, JsonNode.class);

			JsonNode request = responseEntity.getBody();
			request.forEach(categoryNode -> {
				String category = categoryNode.get("category").asText();
				if (category.equals("OTHERS")) {
					List<PurposeResponseDto.purposeListDTO> purposeList = new ArrayList<>();
					categoryNode.get("purposeList").forEach(purposeNode -> {
						String purpose = purposeNode.get("purpose").asText();
						purposeList.add(new PurposeResponseDto.purposeListDTO(purpose));
					});
					response.add(PurposeResponseDto.builder().category(category).purposeList(purposeList).build());
				} else {
					response.add(PurposeResponseDto.categoryDTO.builder().category(category).build());
				}
			});
			logger.info("Response for network ach: {}", response);

			responseDTO.setStatus("Success");
			responseDTO.setMessage("Success");
			responseDTO.setData(response);
			return responseDTO;
		} catch (Exception e) {
			logger.error("Error: {}", e.getMessage());
			responseDTO.setStatus("Failure");
			responseDTO.setMessage("Internal Server Error.");
			responseDTO.setData(new HashMap<>());
			return responseDTO;
		}
	}
}
