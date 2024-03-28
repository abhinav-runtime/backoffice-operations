package com.backoffice.operations.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.backoffice.operations.entity.BoSystemLogEntity;
import com.backoffice.operations.payloads.AccessTokenResponse;
import com.backoffice.operations.payloads.BoSystemDetailsResponseDTO;
import com.backoffice.operations.payloads.BoSystemLogResponseDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.BoLogginglogRepository;
import com.backoffice.operations.repository.CivilIdRepository;
import com.backoffice.operations.repository.SystemDetailRepository;
import com.backoffice.operations.service.BoSystemDetailsService;
import com.backoffice.operations.utils.CommonUtils;
import com.backoffice.operations.utils.DateToStringUtil;
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
	public List<BoSystemDetailsResponseDTO> getSystemDetails(String custNo, int page, int size) {
		List<BoSystemDetailsResponseDTO> responseDTO = new ArrayList<>();
		Pageable pageable = PageRequest.of(page, size);

		systemDetailRepository.findAllByCivilIdOrderByCreatedDesc(custNo, pageable).forEach(Item -> {
			BoSystemDetailsResponseDTO systemDetailsResponseDTO = new BoSystemDetailsResponseDTO();
			String accessToken = null;
			try {
				ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
				logger.info("response: {}", response.getBody());
				accessToken = Objects.requireNonNull(response.getBody().getAccessToken());
				logger.info("accessToken: {}", accessToken);

				String uniqueKey = Item.getUniqueKey();
				String CivilId = civilIdRepository.findById(uniqueKey).get().getEntityId();
				String apiUrl = externalApiUrl + CivilId;
				HttpHeaders headers = new HttpHeaders();
				headers.setBearerAuth(accessToken);
				headers.setContentType(MediaType.APPLICATION_JSON);
				HttpEntity<String> requestEntity = new HttpEntity<>(headers);
				ResponseEntity<String> responseEntity = jwtAuthRestTemplate.exchange(apiUrl, HttpMethod.GET,
						requestEntity, String.class);
//				ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity,
//						String.class);

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
			systemDetailsResponseDTO.setOs_version(Item.getOsVersion());
			systemDetailsResponseDTO.setResolution(Item.getResolution());
			systemDetailsResponseDTO.setCreated(
					Item.getCreated() != null ? DateToStringUtil.convertDateToString(Item.getCreated()) : "");
			responseDTO.add(systemDetailsResponseDTO);
		});
		return responseDTO;
	}

	@Override
	public GenericResponseDTO<Object> getSystemLogs(int page, int size) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		Map<String, Object> data = new HashMap<>();
		List<BoSystemLogResponseDto> logResponse = new ArrayList<>();
		try {
			Map<String, Object> pageinfo = new HashMap<>();
			Pageable pageable = PageRequest.of(page, size);
			Page<BoSystemLogEntity> logPage = loggingRepository.findByOrderByTimestampDesc(pageable);

			logPage.getContent().forEach(element -> {
				BoSystemLogResponseDto logDetails = new BoSystemLogResponseDto();
				logDetails.setId(element.getId());
				logDetails.setRequestUrl(element.getRequestUrl());
				logDetails.setHttpMethod(element.getHttpMethod());
				logDetails.setAuthType(element.getRequestBody());
				logDetails.setResponseStatus(element.getResponseStatus());
				logDetails.setError(element.getError());
				logDetails.setTimestamp(
						element.getTimestamp() != null ? DateToStringUtil.convertDateToString(element.getTimestamp())
								: "");
				logResponse.add(logDetails);
			});

			pageinfo.put("totalPages", logPage.getTotalPages());
			pageinfo.put("currentPage", logPage.getNumber());
			pageinfo.put("totalElements", logPage.getTotalElements());
			pageinfo.put("pageSize", logPage.getSize());
			pageinfo.put("pageContainElements", logPage.getNumberOfElements());

			data.put("logs", logResponse);
			data.put("pageInfo", pageinfo);
			response.setStatus("Success");
			response.setMessage("System Logs");
			response.setData(data);
		} catch (Exception e) {
			logger.error("Error : {}", e.getMessage());
			response.setStatus("Faliure");
			response.setMessage("Something went wrong.");
			response.setData(new HashMap<>());
		}
		return response;
	}
}
