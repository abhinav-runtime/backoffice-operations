package com.backoffice.operations.service.impl;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.backoffice.operations.payloads.CardStatusDto;
import com.backoffice.operations.payloads.CardStatusDto.cardStatusDto;
import com.backoffice.operations.payloads.CreditCardTrasactionChangeDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.CivilIdRepository;
import com.backoffice.operations.service.CardService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.xml.bind.Element;

@Service
public class CardServiceImp implements CardService {
	private static final Logger logger = LoggerFactory.getLogger(CardServiceImp.class);

	@Value("${external.api.credit.cardList.fetch}")
	private String cardServiceUrl;

	@Value("${external.api.blockUnblockCard}")
	private String blockUnblockCardUrl;

	@Autowired
	private CivilIdRepository civilIdRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public GenericResponseDTO<Object> getCardStatus(String uniqueKey) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			String custNo = civilIdRepository.findById(uniqueKey).get().getCivilId();
			String requestBody = "{\r\n   \"customerId\": \"" + custNo + "\"\r\n}";
			String apiUrl = cardServiceUrl;
			HttpHeaders headers = new HttpHeaders();
			headers.add("TENANT", "ALIZZ_UAT");
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
			ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity,
					JsonNode.class);

			CardStatusDto.responseDto responsDto = new CardStatusDto.responseDto();
			JsonNode responseData = responseEntity.getBody().get("result").get("cardList");
			responseData.forEach(element -> {
				if (!element.get("status").asText().equals("REPLACED")) {
					responsDto.setFlag(element.get("status").asText());
				}
			});
			response.setMessage("Success");
			response.setStatus("Success");
			response.setData(responsDto);
			return response;
		} catch (Exception e) {
			logger.error("Error in getCardStatus : {}", e.getMessage());
			response.setStatus("Faluire");
			response.setMessage("Something went wrong.");
			response.setData(new HashMap<>());
			return response;
		}
	}

	@Override
	public GenericResponseDTO<Object> tempBlockAndUnblock(String uniqueKey, CardStatusDto.requestDto requestDto) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			boolean isLocked = false;
			String custNo = civilIdRepository.findById(uniqueKey).get().getCivilId();
			String requestBody = "{\r\n   \"customerId\": \"" + custNo + "\"\r\n}";
			String apiUrl = cardServiceUrl;
			HttpHeaders headers = new HttpHeaders();
			headers.add("TENANT", "ALIZZ_UAT");
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
			ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity,
					JsonNode.class);

			CardStatusDto.requestDto requestData = new CardStatusDto.requestDto();
			cardStatusDto cardStatusDto = new cardStatusDto();
			requestData.setEntityId(custNo);
			requestData.setFlag(requestDto.getFlag());
			requestData.setReason(requestDto.getReason());
			JsonNode responseData = responseEntity.getBody().get("result").get("cardList");
			responseData.forEach(element -> {
				if (!element.get("status").asText().equals("REPLACED")) {
					if (element.get("status").asText().equals("LOCKED")) {
						cardStatusDto.setCardLocked(true);
					}
					if (element.get("status").asText().equals("ALLOCATED")) {
						cardStatusDto.setCardLocked(false);
					}
					String kitNo = element.get("kitNo").asText();
					requestData.setKitNo(kitNo);
				}
			});
			if (cardStatusDto.isCardLocked() && requestDto.getFlag().equals("L")
					|| !cardStatusDto.isCardLocked() && requestDto.getFlag().equals("UL")) {
				response.setStatus("Faluire");
				response.setMessage("Card is already in requested state.");
				response.setData(new HashMap<>());
				return response;
			}

			ObjectMapper objectMapper = new ObjectMapper();
			String body = objectMapper.writeValueAsString(requestData);
			String blockUnblockUrl = blockUnblockCardUrl;
			HttpEntity<String> blockUnblockRequest = new HttpEntity<>(body, headers);
			ResponseEntity<JsonNode> blockUnblockResponse = restTemplate.exchange(blockUnblockUrl, HttpMethod.POST,
					blockUnblockRequest, JsonNode.class);

			response.setMessage("Success");
			response.setStatus("Success");
			response.setData(blockUnblockResponse.getBody());
			return response;
		} catch (Exception e) {
			logger.error("Error in tempBlockAndUnblock : {}", e.getMessage());
			response.setStatus("Faluire");
			response.setMessage("Something went wrong.");
			response.setData(new HashMap<>());
			return response;
		}
	}
}
