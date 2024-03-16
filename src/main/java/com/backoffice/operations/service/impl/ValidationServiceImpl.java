package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.CivilIdEntity;
import com.backoffice.operations.payloads.AccessTokenResponse;
import com.backoffice.operations.payloads.AccountDetails;
import com.backoffice.operations.payloads.CivilIdAPIResponse;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.CivilIdRepository;
import com.backoffice.operations.service.ValidationService;
import com.backoffice.operations.utils.CommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class ValidationServiceImpl implements ValidationService {
	private static final Logger logger = LoggerFactory.getLogger(ValidationServiceImpl.class);

	private final CommonUtils commonUtils;
	@Autowired
	private CivilIdRepository civilIdRepository;

	@Value("${external.api.accounts}")
	private String accountExternalAPI;

	private final RestTemplate restTemplate;

	@Value("${external.api.m2p.civilId}")
	private String civilIdExternalAPI;

	public ValidationServiceImpl(CommonUtils commonUtils, RestTemplate restTemplate) {
		this.commonUtils = commonUtils;
		this.restTemplate = restTemplate;
	}

	@Override
	public GenericResponseDTO<Object> validateUserAccount(String accountNumber) {
		String cifNumber = accountNumber.substring(3, 10);
		return getTokenAndApiResponse(cifNumber)
				.map(details -> details.getResponse().getPayload().getCustSummaryDetails().getIslamicAccounts().stream()
						.anyMatch(accDetails -> accDetails.getAcc().equals(accountNumber)))
				.map(isValidUser -> {
//					String custFullName = isValidUser ? getTokenAndApiResponseForCustomerInformation(cifNumber) : "";
					String custFullName = isValidUser
							? getTokenAndApiResponseForCustomerInformation(getCivilIdFromcif(cifNumber))
							: "";
					GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
					Map<String, Object> data = new HashMap<>();
					data.put("accountNumber", accountNumber);
					data.put("isValidUser", isValidUser);
					data.put("custName", custFullName);
					data.put("message",
							isValidUser ? "Account is linked with the User" : "Account is not linked with the User");
					responseDTO.setStatus("Success");
					responseDTO.setMessage("Success");
					responseDTO.setData(data);
					return responseDTO;
				}).orElseGet(() -> {
					GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
					Map<String, Object> data = new HashMap<>();
					data.put("accountNumber", accountNumber);
					data.put("isValidUser", false);
					data.put("message", "Invalid User");
					responseDTO.setStatus("Success");
					responseDTO.setMessage("Success");
					responseDTO.setData(data);
					return responseDTO;
				});

	}

	private Optional<AccountDetails> getTokenAndApiResponse(String civilId) {
		ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
		if (Objects.nonNull(response.getBody())) {
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(response.getBody().getAccessToken());
			HttpEntity<String> entity = new HttpEntity<>(headers);

			String apiUrl = accountExternalAPI + civilId;
			ResponseEntity<AccountDetails> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, entity,
					AccountDetails.class);
			logger.info("Response from Account API: {}", responseEntity.getBody());
			return Optional.ofNullable(responseEntity.getBody());
		}
		return Optional.empty();
	}

	private String getTokenAndApiResponseForCustomerInformation(String civilId) {
		ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
		if (Objects.nonNull(response.getBody())) {
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(response.getBody().getAccessToken());
			HttpEntity<String> entity = new HttpEntity<>(headers);

			String apiUrl = civilIdExternalAPI + civilId;
			ResponseEntity<CivilIdAPIResponse> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, entity,
					CivilIdAPIResponse.class);
			CivilIdAPIResponse apiResponse = responseEntity.getBody();
			if (apiResponse != null && apiResponse.isSuccess()) {
				CivilIdAPIResponse.CustomerFull customerFull = apiResponse.getResponse().getResult().getCustomerFull();
				if (customerFull != null) {
					return customerFull.getFullname();
				}
			}
		}
		return "";
	}

	private String getCivilIdFromcif(String cifNumber) {
		try {
			CivilIdEntity civilIdEntity = civilIdRepository.findByCivilId(cifNumber).get(0);
			return civilIdEntity.getEntityId();
		} catch (Exception e) {
			logger.error("Error while fetching civilId from cif: {}", e.getMessage());
			return null;
		}
	}
}
