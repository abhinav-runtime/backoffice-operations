package com.backoffice.operations.service.impl;

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

import com.backoffice.operations.entity.BlockUnblockAction;
import com.backoffice.operations.payloads.BlockUnblockActionDTO;
import com.backoffice.operations.payloads.ExternalApiResponseDTO;
import com.backoffice.operations.repository.BlockUnblockActionRepository;
import com.backoffice.operations.repository.CivilIdRepository;
import com.backoffice.operations.service.CivilIdService;

@Service
public class CivilIdServiceImpl implements CivilIdService {

	@Autowired
	private CivilIdRepository civilIdRepository;

	@Value("${external.api.url}")
	private String externalApiUrl;
	@Value("${external.api.fetchAllCustomers}")
	private String fetchAllCustomers;
	@Value("${external.api.blockUnblockCard}")
	private String blockUnblockURL;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private BlockUnblockActionRepository blockUnblockActionRepository;

	@Override
	public Optional<String> getEntityId(String civilId) {
		return civilIdRepository.findEntityIdByCivilId(civilId);
	}

	@Override
	public ExternalApiResponseDTO getCardList(String entityId) {
		String apiUrl = externalApiUrl + "/getCardList";
		HttpHeaders headers = new HttpHeaders();
		headers.add("TENANT", "ALIZZ_UAT");
		headers.setContentType(MediaType.APPLICATION_JSON);
		String requestBody = "{ \"customerId\": \"" + entityId + "\" }";
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
		ResponseEntity<ExternalApiResponseDTO> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST,
				requestEntity, ExternalApiResponseDTO.class);
		return responseEntity.getBody();
	}

	@Override
	public Object fetchAllCustomerData(String entityId) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("TENANT", "ALIZZ_UAT");
		headers.setContentType(MediaType.APPLICATION_JSON);
		String requestBody = "{ \"entityId\": \"" + entityId + "\" }";
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
		ResponseEntity<Object> responseEntity = restTemplate.exchange(fetchAllCustomers, HttpMethod.POST, requestEntity,
				Object.class);
		return responseEntity.getBody();
	}

	@Override
	public Object blockUnblockCard(BlockUnblockActionDTO blockUnblockActionDTO) {
		BlockUnblockAction blockUnblockAction = new BlockUnblockAction();
		blockUnblockAction.setEntityId(blockUnblockActionDTO.getEntityId());
		blockUnblockAction.setKitNo(blockUnblockActionDTO.getKitNo());
		blockUnblockAction.setFlag(blockUnblockActionDTO.getFlag());
		blockUnblockAction.setReason(blockUnblockActionDTO.getReason());
		blockUnblockActionRepository.save(blockUnblockAction);

		HttpHeaders headers = new HttpHeaders();
		headers.add("TENANT", "ALIZZ_UAT");
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<BlockUnblockActionDTO> requestEntity = new HttpEntity<>(blockUnblockActionDTO, headers);
		ResponseEntity<Object> response = restTemplate.postForEntity(blockUnblockURL, requestEntity, Object.class);
		if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
			return response.getBody();
		} else {
			//TODO: log proper exception and map it accordingly.
			return response.getBody();
		}
	}

}
