package com.backoffice.operations.service.impl;

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

import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.BoAccountService;
import com.backoffice.operations.utils.CommonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BoAccountServiceImp implements BoAccountService {
	private final Logger logger = LoggerFactory.getLogger(BoAccountServiceImp.class);
	@Value("${external.api.accounts}")
	private String externalAccountApiUrl;
	@Value("${external.api.accounts.transaction}")
	private String externalTransactionApiUrl;
	@Autowired
	@Qualifier("jwtAuth")
	private RestTemplate jwtAuthRestTemplate;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private CommonUtils commonUtils;

	@Override
	public GenericResponseDTO<Object> getAccountDetails(String custNo) {
		logger.info("Fetching account details for customer number: {}", custNo);
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();

		String accessToken = null;
		try {
			// ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
			// logger.info("response: {}", response.getBody());
			// accessToken = Objects.requireNonNull(response.getBody().getAccessToken());
			// logger.info("accessToken: {}", accessToken);

			// String apiUrl = externalAccountApiUrl + custNo;
			String apiUrl = "http://182.18.138.199/chandan/api/v1/accounts/" + custNo;
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
			JsonNode name = jsonNode.at("/response/payload/custSummaryDetails/islamicAccounts");
			System.out.println(name);
			responseDTO.setData(name);
			responseDTO.setMessage("Account details fetched successfully");
			responseDTO.setStatus("Success");
			return responseDTO;
		} catch (Exception e) {
			logger.error("Error occurred while fetching account details: {}", e.getMessage());
			responseDTO.setData(null);
			responseDTO.setMessage("Something went wrong while fetching account details");
			responseDTO.setStatus("Failure");
			return responseDTO;
		}
	}
	
	
}
