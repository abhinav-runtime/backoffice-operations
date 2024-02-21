package com.backoffice.operations.service.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
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
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.backoffice.operations.entity.AccessToken;
import com.backoffice.operations.entity.BlockUnblockAction;
import com.backoffice.operations.entity.CardEntity;
import com.backoffice.operations.entity.CivilIdEntity;
import com.backoffice.operations.entity.OtpEntity;
import com.backoffice.operations.entity.User;
import com.backoffice.operations.enums.CardStatus;
import com.backoffice.operations.payloads.AccessTokenResponse;
import com.backoffice.operations.payloads.BlockUnblockActionDTO;
import com.backoffice.operations.payloads.CivilIdAPIResponse;
import com.backoffice.operations.payloads.EntityIdDTO;
import com.backoffice.operations.payloads.CivilIdAPIResponse.CustomerFull;
import com.backoffice.operations.payloads.ExternalApiResponseDTO;
import com.backoffice.operations.payloads.ExternalApiResponseDTO.Result.Card;
import com.backoffice.operations.payloads.ValidationResultDTO;
import com.backoffice.operations.repository.AccessTokenRepository;
import com.backoffice.operations.repository.BlockUnblockActionRepository;
import com.backoffice.operations.repository.CardRepository;
import com.backoffice.operations.repository.CivilIdRepository;
import com.backoffice.operations.repository.OtpRepository;
import com.backoffice.operations.repository.UserRepository;
import com.backoffice.operations.security.JwtTokenProvider;
import com.backoffice.operations.service.CivilIdService;

import jakarta.transaction.Transactional;

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
	@Value("${external.api.m2p.token}")
	private String tokenApiUrl;
	@Value("${external.api.m2p.client.id}")
	private String clientId;
	@Value("${external.api.m2p.client.secret}")
	private String clientSecret;
	@Autowired
	private RestTemplate restTemplate;
	@Value("${external.api.m2p.civilId}")
	private String civilIdExternalAPI;
	@Value("${external.api.civilId}")
	private String civilId;
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
	@Autowired
	private AccessTokenRepository accessTokenRepository;

	@Override
	public ValidationResultDTO validateCivilId(String entityId, String token) {
		String userEmail = jwtTokenProvider.getUsername(token);
		Optional<User> user = userRepository.findByEmail(userEmail);
		ValidationResultDTO validationResultDTO = new ValidationResultDTO();
		ValidationResultDTO.Data data = new ValidationResultDTO.Data();
		AccessToken accessToken = null;
		try {
			// GET Access token from M2P and save it into the DB.
//			String requestUrl = tokenApiUrl + "?grant_type=client_credentials&scope=openid profile email&client_id="
//					+ clientId + "&client_secret=" + clientSecret;
//			logger.info("requestUrl: ", requestUrl);
//			ResponseEntity<AccessTokenResponse> response = restTemplate.postForEntity(requestUrl, null,
//					AccessTokenResponse.class);
//			logger.info("response: ", response.getBody());
//			if (response != null) {
//				accessToken = saveAccessToken(response.getBody());
//			}

			Optional<CivilIdEntity> civilIdEntityDB = civilIdRepository.findByEntityId(entityId);
			if (civilIdEntityDB.isPresent()) {
				int allowedAttempts = 3;
				int timeoutSeconds = 180;

				if(StringUtils.hasLength(civilIdEntityDB.get().getCivilId())) {
					validationResultDTO.setStatus("Success");
					validationResultDTO.setMessage("Success");
					data.setUniqueKey(civilIdEntityDB.get().getId().toString());
					validationResultDTO.setData(data);
					return validationResultDTO;
				} else if (civilIdEntityDB.get().getAttempts() < allowedAttempts) {
					// Valid attempt, update the entity
					civilIdEntityDB.get().setAttempts(civilIdEntityDB.get().getAttempts() + 1);
					civilIdEntityDB.get().setLastAttemptTime(LocalDateTime.now());

					civilIdRepository.save(civilIdEntityDB.get());
					
					validationResultDTO.setStatus("Failure");
					validationResultDTO.setMessage("Something went wrong");
					data.setUniqueKey(civilIdEntityDB.get().getId().toString());
					validationResultDTO.setData(data);
					return validationResultDTO;
					
				} else if (ChronoUnit.SECONDS.between(civilIdEntityDB.get().getLastAttemptTime(),
						LocalDateTime.now()) < timeoutSeconds) {
					logger.error("Session Is Blocked");
					validationResultDTO.setStatus("Failure");
					validationResultDTO.setMessage("Session Is Blocked");
					data.setUniqueKey(civilIdEntityDB.get().getId().toString());
					validationResultDTO.setData(data);
					return validationResultDTO;
				} else if (civilIdEntityDB.get().getAttempts() < allowedAttempts) {
					logger.error("Civil Id Blocked, Too many attempts or timeout exceeded");
					validationResultDTO.setStatus("Failure");
					validationResultDTO.setMessage("Civil Id Blocked");
					data.setUniqueKey(civilIdEntityDB.get().getId().toString());
					validationResultDTO.setData(data);
					return validationResultDTO;
				} else {
					logger.error("Device Blocked");
					validationResultDTO.setStatus("Failure");
					validationResultDTO.setMessage("Device Blocked");
					data.setUniqueKey(civilIdEntityDB.get().getId().toString());
					validationResultDTO.setData(data);
					return validationResultDTO;
				}
			} else {
				CivilIdEntity civilIdEntity = new CivilIdEntity();
				civilIdEntity.setEntityId(entityId);
				civilIdEntity.setUserId(user.get().getId().toString());
				try {
					if (user.isPresent()) {
						
						ResponseEntity<CivilIdAPIResponse> responseEntity = null;
						if (Objects.nonNull(accessToken)) {
							HttpHeaders headers = new HttpHeaders();
							headers.setBearerAuth(accessToken.getAccessToken());
							HttpEntity<String> entity = new HttpEntity<>(headers);
							String apiUrl = civilIdExternalAPI + entityId;
							responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, entity,
									CivilIdAPIResponse.class);
						} else {
							String apiUrl = civilId + entityId;
							responseEntity = restTemplate.getForEntity(apiUrl, CivilIdAPIResponse.class);
						}
						CivilIdAPIResponse apiResponse = responseEntity.getBody();
						if (apiResponse != null && apiResponse.isSuccess()) {
							CustomerFull customerFull = apiResponse.getResponse().getResult().getCustomerFull();

							if (customerFull != null) {
								civilIdEntity.setCivilId(customerFull.getCustNo());
								civilIdRepository.save(civilIdEntity);

								validationResultDTO.setStatus("Success");
								validationResultDTO.setMessage("Success");
								data.setUniqueKey(civilIdEntity.getId().toString());
								validationResultDTO.setData(data);
								return validationResultDTO;
							}
						}
					}
					civilIdRepository.save(civilIdEntity);

					validationResultDTO.setMessage("Failure");
					validationResultDTO.setStatus("Something went wrong");
					data.setUniqueKey(civilIdEntity.getId().toString());
					validationResultDTO.setData(data);
					return validationResultDTO;

				} catch (Exception e) {
					civilIdRepository.save(civilIdEntity);

					logger.error("ERROR in class CivilIdServiceImpl method validateCivilId", e);
					validationResultDTO.setStatus("Failure");
					validationResultDTO.setMessage("Something went wrong");
					data.setUniqueKey(civilIdEntity.getId().toString());
					validationResultDTO.setData(data);
					return validationResultDTO;
				}
			}
		} catch (Exception e) {
			logger.error("Error in calling token API : ", e);
			validationResultDTO.setStatus("Failure");
			validationResultDTO.setMessage("Something went wrong");
			validationResultDTO.setData(data);
			return validationResultDTO;
		}
	}

	@Transactional
	public AccessToken saveAccessToken(AccessTokenResponse accessTokenResponse) {
		AccessToken accessToken = new AccessToken();
		accessToken.setAccessToken(accessTokenResponse.getAccessToken());
		accessToken.setExpiresIn(accessTokenResponse.getExpiresIn());
		accessToken.setCreatedAt(LocalDateTime.now());
		accessTokenRepository.save(accessToken);
		return accessToken;
	}

	@Override
	public ValidationResultDTO verifyCard(EntityIdDTO entityIdDTO, String token) {
		logger.debug("In class CivilIdServiceImpl method getCardList");
		String userEmail = jwtTokenProvider.getUsername(token);
		Optional<User> user = userRepository.findByEmail(userEmail);
		ValidationResultDTO validationResultDTO = new ValidationResultDTO();
		ValidationResultDTO.Data data = new ValidationResultDTO.Data();
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

									validationResultDTO.setStatus("Success");
									validationResultDTO.setMessage("Your card is locked");
									data.setUniqueKey(entityIdDTO.getUniqueKey());
									validationResultDTO.setData(data);
									return validationResultDTO;
								} else if (card.getStatus().equalsIgnoreCase(CardStatus.BLOCKED.name())) {
									validationResultDTO.setStatus("Failure");
									validationResultDTO.setMessage("Your card is permanently blocked");
									data.setUniqueKey(entityIdDTO.getUniqueKey());
									validationResultDTO.setData(data);
									return validationResultDTO;
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

									validationResultDTO.setStatus("Success");
									validationResultDTO.setMessage("Success");
									data.setUniqueKey(entityIdDTO.getUniqueKey());
									validationResultDTO.setData(data);
									return validationResultDTO;
								} else {
									validationResultDTO.setStatus("Failure");
									validationResultDTO.setMessage("Something went wrong");
									data.setUniqueKey(entityIdDTO.getUniqueKey());
									validationResultDTO.setData(data);
									return validationResultDTO;
								}
							}
						}
					}
				} else {
					validationResultDTO.setStatus("Failure");
					validationResultDTO.setMessage("Something went wrong");
					data.setUniqueKey(entityIdDTO.getUniqueKey());
					validationResultDTO.setData(data);
					return validationResultDTO;
				}
			}
			validationResultDTO.setStatus("Failure");
			validationResultDTO.setMessage("Something went wrong");
			data.setUniqueKey(entityIdDTO.getUniqueKey());
			validationResultDTO.setData(data);
			return validationResultDTO;
		} catch (Exception e) {
			logger.error("ERROR in class CivilIdServiceImpl method verifyCard", e);
			validationResultDTO.setStatus("Failure");
			validationResultDTO.setMessage("Something went wrong");
			data.setUniqueKey(entityIdDTO.getUniqueKey());
			validationResultDTO.setData(data);
			return validationResultDTO;
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
			// TODO: log proper exception and map it accordingly.
			return response.getBody();
		}
	}

}
