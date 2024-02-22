package com.backoffice.operations.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.backoffice.operations.entity.BlockUnblockAction;
import com.backoffice.operations.entity.CardEntity;
import com.backoffice.operations.entity.CivilIdEntity;
import com.backoffice.operations.entity.OtpEntity;
import com.backoffice.operations.entity.User;
import com.backoffice.operations.enums.CardStatus;
import com.backoffice.operations.payloads.BlockUnblockActionDTO;
import com.backoffice.operations.payloads.CivilIdAPIResponse;
import com.backoffice.operations.payloads.EntityIdDTO;
import com.backoffice.operations.payloads.CivilIdAPIResponse.CustomerFull;
import com.backoffice.operations.payloads.ExternalApiResponseDTO;
import com.backoffice.operations.payloads.ExternalApiResponseDTO.Result.Card;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.BlockUnblockActionRepository;
import com.backoffice.operations.repository.CardRepository;
import com.backoffice.operations.repository.CivilIdRepository;
import com.backoffice.operations.repository.OtpRepository;
import com.backoffice.operations.repository.UserRepository;
import com.backoffice.operations.security.JwtTokenProvider;
import com.backoffice.operations.service.CivilIdService;

@Service
public class CivilIdServiceImpl implements CivilIdService {

	private static final Logger logger = LoggerFactory.getLogger(CivilIdServiceImpl.class);

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
	@Value("${external.api.civilId}")
	private String civilIdExternalAPI;
	@Autowired
	private BlockUnblockActionRepository blockUnblockActionRepository;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private OtpRepository otpRepository;
	@Autowired
	private CardRepository cardRepository;

	@Override
	public GenericResponseDTO<Object> validateCivilId(String entityId, String token) {
		
		String userEmail = jwtTokenProvider.getUsername(token);
		Optional<User> user = userRepository.findByEmail(userEmail);
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		CivilIdEntity civilIdEntity = new CivilIdEntity();
		civilIdEntity.setEntityId(entityId);
		civilIdEntity.setUserId(user.get().getId().toString());
		try {
			if (user.isPresent()) {
				String apiUrl = civilIdExternalAPI + entityId;
				ResponseEntity<CivilIdAPIResponse> responseEntity = restTemplate.getForEntity(apiUrl,
						CivilIdAPIResponse.class);
				CivilIdAPIResponse apiResponse = responseEntity.getBody();

				if (apiResponse != null && apiResponse.isSuccess()) {
					CustomerFull customerFull = apiResponse.getResponse().getResult().getCustomerFull();

					if (customerFull != null) {
						civilIdEntity.setCivilId(customerFull.getCustNo());
						civilIdRepository.save(civilIdEntity);
						
						Map<String, String> data = new HashMap<>();
						data.put("uniqueKey", civilIdEntity.getId().toString());
						
						responseDTO.setStatus("Success");
						responseDTO.setMessage("Success");
						responseDTO.setData(data);
						return responseDTO;
					}
				}
			}
			civilIdRepository.save(civilIdEntity);
			
			Map<String, String> data = new HashMap<>();
			data.put("uniqueKey", civilIdEntity.getId().toString());
			
			responseDTO.setMessage("Failure");
			responseDTO.setStatus("Something went wrong");
			responseDTO.setData(data);
			return responseDTO;
		} catch (Exception e) {
			civilIdRepository.save(civilIdEntity);

			logger.error("ERROR in class CivilIdServiceImpl method validateCivilId", e);
			Map<String, String> data = new HashMap<>();
			data.put("uniqueKey", civilIdEntity.getId().toString());
			responseDTO.setStatus("Failure");
			responseDTO.setMessage("Something went wrong");
			responseDTO.setData(data);
			return responseDTO;
		}
	}

	@Override
	public GenericResponseDTO<Object> verifyCard(EntityIdDTO entityIdDTO, String token) {
		logger.debug("In class CivilIdServiceImpl method getCardList");
		String userEmail = jwtTokenProvider.getUsername(token);
		Optional<User> user = userRepository.findByEmail(userEmail);
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		
//		ValidationResultDTO.Data data = new ValidationResultDTO.Data();
		try {
			if (user.isPresent()) {
				Optional<CivilIdEntity> civilIdEntity = civilIdRepository.findById(entityIdDTO.getUniqueKey());
				if (civilIdEntity.isPresent()) {
					String apiUrl = externalApiUrl + "/getCardList";
					HttpHeaders headers = new HttpHeaders();
					headers.add("TENANT", "ALIZZ_UAT");
					headers.setContentType(MediaType.APPLICATION_JSON);
					String requestBody = "{ \"customerId\": \"" + civilIdEntity.get().getCivilId() + "\" }";
					HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
					ResponseEntity<ExternalApiResponseDTO> responseEntity = restTemplate.exchange(apiUrl,
							HttpMethod.POST, requestEntity, ExternalApiResponseDTO.class);
					if (responseEntity != null && responseEntity.getBody().getResult() != null) {
						List<Card> cardList = responseEntity.getBody().getResult().getCardList();
						for (Card card : cardList) {
							String actualFirstFourDigits = card.getCardNo().substring(0, 4);
							String actualLastFourDigits = card.getCardNo().substring(card.getCardNo().length() - 4);
							if (entityIdDTO.getFirstFourDigitscardNo().equals(actualFirstFourDigits)
									&& entityIdDTO.getLastFourDigitscardNo().equals(actualLastFourDigits)) {
								if (card.getStatus().equalsIgnoreCase(CardStatus.LOCKED.name())) {
									
									Map<String, String> data = new HashMap<>();
									data.put("uniqueKey", entityIdDTO.getUniqueKey());
									responseDTO.setStatus("Success");
									responseDTO.setMessage("Your card is locked");
									responseDTO.setData(data);
									return responseDTO;
								} else if (card.getStatus().equalsIgnoreCase(CardStatus.BLOCKED.name())) {
									Map<String, String> data = new HashMap<>();
									data.put("uniqueKey", entityIdDTO.getUniqueKey());
									responseDTO.setStatus("Failure");
									responseDTO.setMessage("Your card is permanently blocked");
									responseDTO.setData(data);
									return responseDTO;
								} else if (card.getStatus().equalsIgnoreCase(CardStatus.ALLOCATED.name())) {
									OtpEntity otpEntity = otpRepository
											.findByUniqueKeyCivilId(civilIdEntity.get().getId().toString());
									otpEntity = new OtpEntity();
									// Set uniqueid against civil id.
									otpEntity.setUniqueKeyCivilId(civilIdEntity.get().getId().toString());
//									String newOtp = CommonUtils.generateRandomOtp();
									String newOtp = "1234";
									otpEntity.setOtp(newOtp);
									otpEntity.setLastAttemptTime(LocalDateTime.now());
									otpRepository.save(otpEntity);

									CardEntity cardEntity = new CardEntity();
									cardEntity.setUniqueKeyCivilId(civilIdEntity.get().getId().toString());
									cardEntity.setCivilId(civilIdEntity.get().getCivilId());
									cardEntity.setCardKitNo(card.getKitNo());
									cardEntity.setExpiry(card.getExpiryDate());
									cardEntity.setDobOfUser(responseEntity.getBody().getResult().getDob());
									cardRepository.save(cardEntity);
									Map<String, String> data = new HashMap<>();
									data.put("uniqueKey", entityIdDTO.getUniqueKey());
									responseDTO.setStatus("Success");
									responseDTO.setMessage("Success");
									responseDTO.setData(data);
									return responseDTO;
								} else {
									Map<String, String> data = new HashMap<>();
									data.put("uniqueKey", entityIdDTO.getUniqueKey());
									responseDTO.setStatus("Failure");
									responseDTO.setMessage("Something went wrong");
									responseDTO.setData(data);
									return responseDTO;
								}
							}
						}
					}
				} else {
					Map<String, String> data = new HashMap<>();
					data.put("uniqueKey", entityIdDTO.getUniqueKey());
					responseDTO.setStatus("Failure");
					responseDTO.setMessage("Something went wrong");
					responseDTO.setData(data);
					return responseDTO;
				}
			}
			Map<String, String> data = new HashMap<>();
			data.put("uniqueKey", entityIdDTO.getUniqueKey());
			responseDTO.setStatus("Failure");
			responseDTO.setMessage("Something went wrong");
			responseDTO.setData(data);
			return responseDTO;
		} catch (Exception e) {
			logger.error("ERROR in class CivilIdServiceImpl method verifyCard", e);
			Map<String, String> data = new HashMap<>();
			data.put("uniqueKey", entityIdDTO.getUniqueKey());
			responseDTO.setStatus("Failure");
			responseDTO.setMessage("Something went wrong");
			responseDTO.setData(data);
			return responseDTO;
		}
	}

	@Override
	public Object fetchAllCustomerData(EntityIdDTO entityIdDTO, String token) {
		Optional<CivilIdEntity> civilIdEntity = civilIdRepository.findById(entityIdDTO.getUniqueKey());
		if (civilIdEntity.isPresent()) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("TENANT", "ALIZZ_UAT");
			headers.setContentType(MediaType.APPLICATION_JSON);
			String requestBody = "{ \"entityId\": \"" + civilIdEntity.get().getCivilId() + "\" }";
			HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
			ResponseEntity<Object> responseEntity = restTemplate.exchange(fetchAllCustomers, HttpMethod.POST,
					requestEntity, Object.class);
			return responseEntity.getBody();
		}
		return null;
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
			return response.getBody();
		}
	}

}
