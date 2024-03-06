package com.backoffice.operations.service.impl;

import java.util.HashMap;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.BoCarddetailService;
import com.backoffice.operations.utils.CommonUtils;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class BoCardDetailServiceImp implements BoCarddetailService {
	private static final Logger logger = LoggerFactory.getLogger(BoCardDetailServiceImp.class);
	@Value("${external.api.url}")
	private String externalApiUrl;
	@Value("${external.api.fetchPreferenceUrl}")
	private String fetchPrefarenceApiUrl;
	@Value("${external.api.setPreferenceUrl}")
	private String setPrefarenceApiUrl;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public GenericResponseDTO<Object> fetchCardDeatils(String custNo) {
		logger.info("Fetching account details for customer number: {}", custNo);
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		try {

			String requestBody = "{\r\n    \"customerId\": \"" + custNo + "\"\r\n}";

			String apiUrl = externalApiUrl + "/getCardList";
			HttpHeaders headers = new HttpHeaders();
			headers.add("TENANT", "ALIZZ_UAT");
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
			ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity,
					JsonNode.class);
			responseDTO.setData(responseEntity.getBody());
			responseDTO.setMessage("Account details fetched successfully");
			responseDTO.setStatus("Success");
			return responseDTO;
		} catch (Exception e) {
			logger.error("Error occurred while fetching account details: {}", e.getMessage());
			responseDTO.setData(new HashMap<>());
			responseDTO.setMessage("Something went wrong while fetching account details");
			responseDTO.setStatus("Failure");
			return responseDTO;
		}
	}

	@Override
	public GenericResponseDTO<Object> fetchPreference(String custNo) {
		logger.info("Fetching preferance : {}", custNo);
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		try {

			String requestBody = "{\r\n   \"entityId\": \"" + custNo + "\"\r\n}";

			String apiUrl = fetchPrefarenceApiUrl;
			HttpHeaders headers = new HttpHeaders();
			headers.add("TENANT", "ALIZZ_UAT");
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
			ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity,
					JsonNode.class);
			responseDTO.setData(responseEntity.getBody());
			responseDTO.setMessage("Preference fetched successfully");
			responseDTO.setStatus("Success");
			return responseDTO;
		} catch (Exception e) {
			logger.error("Error occurred while fetching preference: {}", e.getMessage());
			responseDTO.setData(new HashMap<>());
			responseDTO.setMessage("Something went wrong while fetching details");
			responseDTO.setStatus("Failure");
			return responseDTO;
		}
	}

	@Override
	public GenericResponseDTO<Object> setPreference(JsonNode preference) {
		logger.info("setting preference details : {}", preference);
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		try {
			String apiUrl = setPrefarenceApiUrl;
			HttpHeaders headers = new HttpHeaders();
			headers.add("TENANT", "ALIZZ_UAT");
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<>(preference.toString(), headers);
			ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity,
					JsonNode.class);
			responseDTO.setData(responseEntity.getBody());
			responseDTO.setMessage("Preference set successfully");
			responseDTO.setStatus("Success");
			return responseDTO;
		} catch (Exception e) {
			logger.error("Error occurred while fetching account details: {}", e.getMessage());
			responseDTO.setData(new HashMap<>());
			responseDTO.setMessage("Something went wrong setting preference details");
			responseDTO.setStatus("Failure");
			return responseDTO;
		}
	}

}
