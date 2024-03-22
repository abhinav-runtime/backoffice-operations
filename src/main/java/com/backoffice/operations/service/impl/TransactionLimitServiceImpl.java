package com.backoffice.operations.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.backoffice.operations.entity.TransactionLimitsEntity;
import com.backoffice.operations.entity.TransactionMaxMinLimitsParameter;
import com.backoffice.operations.entity.User;
import com.backoffice.operations.payloads.CardSettingDto;
import com.backoffice.operations.payloads.CreditCardTrasactionChangeDto;
import com.backoffice.operations.payloads.CreditCardTrasactionDto;
import com.backoffice.operations.payloads.PurposeResponseDto;
import com.backoffice.operations.payloads.TransactionLimitsDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.CardRepository;
import com.backoffice.operations.repository.CivilIdRepository;
import com.backoffice.operations.repository.TransactionLimitsRepository;
import com.backoffice.operations.repository.TransactionMaxMinLimitsParameterRepo;
import com.backoffice.operations.repository.UserRepository;
import com.backoffice.operations.security.JwtTokenProvider;
import com.backoffice.operations.service.TransactionLimitService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.xml.bind.Element;

@Service
public class TransactionLimitServiceImpl implements TransactionLimitService {
	private final static Logger logger = LoggerFactory.getLogger(TransactionLimitServiceImpl.class);

	@Value("${external.api.setPreferenceUrl}")
	private String setPreferenceUrl;

	@Value("${external.api.fetchPreferenceUrl}")
	private String fetchPreferenceUrl;
	@Autowired
	private RestTemplate basicAuthRestTemplate;

	@Autowired
	private CardRepository cardRepository;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TransactionMaxMinLimitsParameterRepo transactionMaxLimitsParameterRepo;

	@Autowired
	private TransactionLimitsRepository transactionLimitsRepository;

	@Autowired
	private CivilIdRepository civilIdRepository;

	@Override
	public GenericResponseDTO<Object> setTransactionLimits(TransactionLimitsDTO transactionLimitsDTO) {

		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		try {
			// Fetching Previous Limits
			String custNo = civilIdRepository.findById(transactionLimitsDTO.getUniqueKey()).get().getCivilId();
			CreditCardTrasactionChangeDto creditCardTrasactionChangeDto = new CreditCardTrasactionChangeDto();
			String requestBody = "{\r\n   \"entityId\": \"" + custNo + "\"\r\n}";
			String apiUrl = fetchPreferenceUrl;
			HttpHeaders headers = new HttpHeaders();
			headers.add("TENANT", "ALIZZ_UAT");
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
			ResponseEntity<JsonNode> responseEntity = basicAuthRestTemplate.exchange(apiUrl, HttpMethod.POST,
					requestEntity, JsonNode.class);

			JsonNode response = responseEntity.getBody().get("result");
			creditCardTrasactionChangeDto = getCreditCardTrasactionChangeDto(response);
			creditCardTrasactionChangeDto.setEntityId(custNo);

			// Set New Limits
			creditCardTrasactionChangeDto.setLimitConfigs(makeChanges(creditCardTrasactionChangeDto.getLimitConfigs(),
					transactionLimitsDTO.getNewLimits(), transactionLimitsDTO.getRequestType()));
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonString = "";
			jsonString = objectMapper.writeValueAsString(creditCardTrasactionChangeDto);
			String setUrl = setPreferenceUrl;
			HttpEntity<String> setRequest = new HttpEntity<>(jsonString, headers);
			logger.info("Request Body: {}", setRequest);
			ResponseEntity<JsonNode> setResponse = basicAuthRestTemplate.exchange(setUrl, HttpMethod.POST, setRequest,
					JsonNode.class);

			responseDTO.setStatus("Success");
			responseDTO.setMessage("Transaction limits set successfully!");
			responseDTO.setData(setResponse.getBody());
		} catch (Exception e) {
			logger.error("Error in setTransactionLimits : {}", e.getMessage());
			responseDTO.setStatus("Failure");
			responseDTO.setMessage("Something went wrong");
			responseDTO.setData(new HashMap<>());
		}
		return responseDTO;
	}

	@Override
	public GenericResponseDTO<Object> getAllTransactionLimits(String token) {

		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		String userEmail = jwtTokenProvider.getUsername(token);
		Optional<User> user = userRepository.findByEmail(userEmail);

		if (user.isPresent()) {
			List<TransactionLimitsEntity> transactionLimitsEntity = transactionLimitsRepository.findAll();
			Map<String, Object> data = new HashMap<>();
			responseDTO.setStatus("Success");
			responseDTO.setMessage("Customers transaction limits list!");
			data.put("list", transactionLimitsEntity);
			responseDTO.setData(data);
			return responseDTO;
		}
		responseDTO.setStatus("Failure");
		responseDTO.setMessage("Something went wrong");
		return responseDTO;
	}

	@Override
	public GenericResponseDTO<Object> getTransactionLimitsByCustId(String uniqueKey) {

		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		String custNo = "";
		try {
			if (civilIdRepository.existsById(uniqueKey)) {
				custNo = civilIdRepository.findById(uniqueKey).get().getCivilId();
				logger.info("Fetching preferance : {}", custNo);
			}

			String requestBody = "{\r\n   \"entityId\": \"" + custNo + "\"\r\n}";

			String apiUrl = fetchPreferenceUrl;
			HttpHeaders headers = new HttpHeaders();
			headers.add("TENANT", "ALIZZ_UAT");
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
			ResponseEntity<JsonNode> responseEntity = basicAuthRestTemplate.exchange(apiUrl, HttpMethod.POST,
					requestEntity, JsonNode.class);

			JsonNode response = responseEntity.getBody().get("result");
			CreditCardTrasactionDto creditCardTrasactionDto = new CreditCardTrasactionDto();
			List<CreditCardTrasactionDto.LimitConfig> limitConfigs = new ArrayList<>();

			response.get("limitConfig").forEach(configNode -> {
				String channelType = configNode.get("channelType").asText();
				String maxAmount = configNode.get("maxAmount").asText();

				CreditCardTrasactionDto.LimitConfig limitConfig = CreditCardTrasactionDto.LimitConfig.builder()
						.channelType(channelType).maxAmount(maxAmount).build();

				limitConfigs.add(limitConfig);
			});

			creditCardTrasactionDto.setLimitConfig(limitConfigs);

			responseDTO.setData(creditCardTrasactionDto);
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
	public GenericResponseDTO<Object> getCardSetting(String uniqueKey) {
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		String custNo = "";
		try {
			if (civilIdRepository.existsById(uniqueKey)) {
				custNo = civilIdRepository.findById(uniqueKey).get().getCivilId();
				logger.info("Fetching preferance : {}", custNo);
			}

			String requestBody = "{\r\n   \"entityId\": \"" + custNo + "\"\r\n}";

			String apiUrl = fetchPreferenceUrl;
			HttpHeaders headers = new HttpHeaders();
			headers.add("TENANT", "ALIZZ_UAT");
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
			ResponseEntity<JsonNode> responseEntity = basicAuthRestTemplate.exchange(apiUrl, HttpMethod.POST,
					requestEntity, JsonNode.class);

			JsonNode response = responseEntity.getBody().get("result");

			CardSettingDto cardSettingDto = new CardSettingDto();
			cardSettingDto.setAtm(response.get("atm").asBoolean());
			cardSettingDto.setPos(response.get("pos").asBoolean());
			cardSettingDto.setEcom(response.get("ecom").asBoolean());
			cardSettingDto.setContactless(response.get("contactless").asBoolean());

			responseDTO.setData(cardSettingDto);
			responseDTO.setMessage("Preference fetched successfully");
			responseDTO.setStatus("Success");
			return responseDTO;

		} catch (Exception e) {
			logger.error("Error on getCardSetting : {}", e.getMessage());
			responseDTO.setData(new HashMap<>());
			responseDTO.setMessage("Something went wrong");
			responseDTO.setStatus("Failure");
			return responseDTO;
		}
	}

	@Override
	public GenericResponseDTO<Object> setCardSetting(CardSettingDto cardSettingDto, String uniqueKey) {
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		try {
			// Fetching Previous Limits
			String custNo = civilIdRepository.findById(uniqueKey).get().getCivilId();
			CreditCardTrasactionChangeDto creditCardTrasactionChangeDto = new CreditCardTrasactionChangeDto();
			String requestBody = "{\r\n   \"entityId\": \"" + custNo + "\"\r\n}";
			String apiUrl = fetchPreferenceUrl;
			HttpHeaders headers = new HttpHeaders();
			headers.add("TENANT", "ALIZZ_UAT");
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
			ResponseEntity<JsonNode> responseEntity = basicAuthRestTemplate.exchange(apiUrl, HttpMethod.POST,
					requestEntity, JsonNode.class);

			JsonNode response = responseEntity.getBody().get("result");
			creditCardTrasactionChangeDto = getCreditCardTrasactionChangeDto(response);
			creditCardTrasactionChangeDto.setEntityId(custNo);

			// Set New Limits
			creditCardTrasactionChangeDto.setAtm(cardSettingDto.getAtm());
			creditCardTrasactionChangeDto.setPos(cardSettingDto.getPos());
			creditCardTrasactionChangeDto.setEcom(cardSettingDto.getEcom());
			creditCardTrasactionChangeDto.setContactless(cardSettingDto.getContactless());
			
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonString = "";
			jsonString = objectMapper.writeValueAsString(creditCardTrasactionChangeDto);
			String setUrl = setPreferenceUrl;
			HttpEntity<String> setRequest = new HttpEntity<>(jsonString, headers);
			logger.info("Request Body: {}", setRequest);
			ResponseEntity<JsonNode> setResponse = basicAuthRestTemplate.exchange(setUrl, HttpMethod.POST, setRequest,
					JsonNode.class);

			responseDTO.setStatus("Success");
			responseDTO.setMessage("Card Setting set successfully!");
			responseDTO.setData(setResponse.getBody());
		} catch (Exception e) {
			logger.error("Error in setCardSetting : {}", e.getMessage());
			responseDTO.setStatus("Failure");
			responseDTO.setMessage("Something went wrong");
			responseDTO.setData(new HashMap<>());
		}
		return responseDTO;
	}

	private CreditCardTrasactionChangeDto getCreditCardTrasactionChangeDto(JsonNode jsonData) {
		CreditCardTrasactionChangeDto creditCardTrasactionChangeDto = new CreditCardTrasactionChangeDto();
		creditCardTrasactionChangeDto.setInternational(jsonData.get("international").asBoolean());
		creditCardTrasactionChangeDto.setDcc(jsonData.get("dcc").asBoolean());
		creditCardTrasactionChangeDto.setCurrency("OMR");
		creditCardTrasactionChangeDto.setIsLimitUpgrade(true);
		creditCardTrasactionChangeDto.setIsOverLimitAllowed(false);
		creditCardTrasactionChangeDto.setPreferredLanguage("ES");
		creditCardTrasactionChangeDto.setTransactionUsageType("DOMESTIC");
		creditCardTrasactionChangeDto.setAtm(jsonData.get("atm").asBoolean());
		creditCardTrasactionChangeDto.setEcom(jsonData.get("ecom").asBoolean());
		creditCardTrasactionChangeDto.setPos(jsonData.get("pos").asBoolean());
		creditCardTrasactionChangeDto.setContactless(jsonData.get("contactless").asBoolean());
		creditCardTrasactionChangeDto.setDisallowedRuleConfig(jsonData.get("disallowedRuleConfig"));
		List<CreditCardTrasactionChangeDto.LimitConfigs> limitConfigs = new ArrayList<>();
		jsonData.get("limitConfig").forEach(element -> {
			CreditCardTrasactionChangeDto.LimitConfigs limitConfig = new CreditCardTrasactionChangeDto.LimitConfigs();
			limitConfig.setChannel(element.get("channelType").asText());
			limitConfig.setTxnType(element.get("txnType").asText());
			limitConfig.setDailyLimitValue(element.get("dailyLimitValue").asDouble());
			limitConfig.setDailyLimitCnt(element.get("dailyLimitCnt").asDouble());
			limitConfig.setMonthlyLimitValue(element.get("monthlyLimitValue").asDouble());
			limitConfig.setMonthlyLimitCnt(element.get("monthlyLimitCnt").asDouble());
			limitConfig.setYearlyLimitValue(element.get("yearlyLimitValue").asDouble());
			limitConfig.setYearlyLimitCnt(element.get("yearlyLimitCnt").asDouble());
			limitConfig.setMinAmount(element.get("minAmount").asDouble());
			limitConfig.setMaxAmount(element.get("maxAmount").asDouble());
			limitConfig.setCurrency(element.get("currency").asText());
			limitConfig.setCategory(element.get("category").asText());
			limitConfigs.add(limitConfig);
		});
		creditCardTrasactionChangeDto.setLimitConfigs(limitConfigs);
		return creditCardTrasactionChangeDto;
	}

	private List<CreditCardTrasactionChangeDto.LimitConfigs> makeChanges(
			List<CreditCardTrasactionChangeDto.LimitConfigs> limiConfigs, int updateLimits, String channelType) {
		limiConfigs.forEach(element -> {
			if (element.getChannel().equals(channelType)) {
				element.setMaxAmount(updateLimits);
			}
		});
		return limiConfigs;
	}
}
