package com.backoffice.operations.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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

import com.backoffice.operations.payloads.AccessTokenResponse;
import com.backoffice.operations.payloads.BoSystemDetailsResponseDTO;
import com.backoffice.operations.payloads.BoSystemLogResponseDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.BoLogginglogRepository;
import com.backoffice.operations.repository.CivilIdRepository;
import com.backoffice.operations.repository.SystemDetailRepository;
import com.backoffice.operations.service.BoSystemDetailsService;
import com.backoffice.operations.utils.CommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BoSystemDetailsServiceImp implements BoSystemDetailsService {
	private final Logger logger = LoggerFactory.getLogger(BoSystemDetailsServiceImp.class);
	@Value("${external.api.m2p.civilId}")
	private String externalApiUrl;
	@Autowired
	private SystemDetailRepository systemDetailRepository;
	@Autowired
	private CivilIdRepository civilIdRepository;
	@Autowired
	private BoLogginglogRepository loggingRepository;
	@Autowired
	@Qualifier("jwtAuth")
	private RestTemplate jwtAuthRestTemplate;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private CommonUtils commonUtils;

	@Override
	public List<BoSystemDetailsResponseDTO> getSystemDetails(String custNo) {
		List<BoSystemDetailsResponseDTO> responseDTO = new ArrayList<>();

		systemDetailRepository.findAllByCivilId(custNo).forEach(Item -> {
			BoSystemDetailsResponseDTO systemDetailsResponseDTO = new BoSystemDetailsResponseDTO();
			String accessToken = null;
			try {
				// ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
				// logger.info("response: {}", response.getBody());
				// accessToken = Objects.requireNonNull(response.getBody().getAccessToken());
				// logger.info("accessToken: {}", accessToken);

				String uniqueKey = Item.getUniqueKey();
				String CivilId = civilIdRepository.findById(uniqueKey).get().getEntityId();
				// String apiUrl = externalApiUrl + CivilId;
				String apiUrl = "http://182.18.138.199/chandan/api/v1/customer/nid/" + CivilId;
				HttpHeaders headers = new HttpHeaders();
				headers.setBearerAuth(accessToken);
				headers.setContentType(MediaType.APPLICATION_JSON);
				HttpEntity<String> requestEntity = new HttpEntity<>(headers);
				// ResponseEntity<String> responseEntity = jwtAuthRestTemplate.exchange(apiUrl,
				// HttpMethod.GET,
				// requestEntity, String.class);
				ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity,
						String.class);

				String jsonResponse = responseEntity.getBody();
				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonNode;

				jsonNode = mapper.readTree(jsonResponse);
				String name = jsonNode.at("/response/result/customerFull/name").asText();
				systemDetailsResponseDTO.setName(name);
			} catch (Exception e) {
				logger.error("Error : {}", e.getMessage());
				systemDetailsResponseDTO.setName("NA");
			}
			systemDetailsResponseDTO.setDeviceId(Item.getDeviceId());
			systemDetailsResponseDTO.setType(Item.getType());
			systemDetailsResponseDTO.setModel(Item.getModel());
			systemDetailsResponseDTO.setStatus(Item.getStatus());
			systemDetailsResponseDTO.setAppVersion(Item.getAppVersion());
			systemDetailsResponseDTO.setCarrier(Item.getCarrier());
			systemDetailsResponseDTO.setLocation(Item.getLocation());
			systemDetailsResponseDTO.setIPAddress(Item.getIpAddress());
			responseDTO.add(systemDetailsResponseDTO);
		});
		return responseDTO;
	}

	@Override
	public GenericResponseDTO<Object> getSystemLogs() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		List<BoSystemLogResponseDto> logResponse  = new ArrayList<>();
		try {
			loggingRepository.findAllByOrderByTimestampDesc().forEach(element ->{
			BoSystemLogResponseDto logDetails = new BoSystemLogResponseDto();
			logDetails.setId(element.getId());
			logDetails.setRequestUrl(element.getRequestUrl());
			logDetails.setHttpMethod(element.getHttpMethod());
			logDetails.setAuthType(element.getRequestBody());
			logDetails.setResponseStatus(element.getResponseStatus());
			logDetails.setError(element.getError());
			logDetails.setTimestamp(element.getTimestamp().toLocaleString());
			logResponse.add(logDetails);
		});
		
			response.setStatus("Success");
			response.setMessage("System Logs");
			response.setData(logResponse);
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			response.setStatus("Faliure");
			response.setMessage("Something went wrong.");
			response.setData(new HashMap<>());
		}
		return response;
	}
}
