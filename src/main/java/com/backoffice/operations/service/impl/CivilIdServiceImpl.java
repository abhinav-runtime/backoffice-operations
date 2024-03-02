package com.backoffice.operations.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

import com.backoffice.operations.payloads.*;
import com.backoffice.operations.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.backoffice.operations.entity.AccessToken;
import com.backoffice.operations.entity.BlockUnblockAction;
import com.backoffice.operations.entity.CardEntity;
import com.backoffice.operations.entity.CivilIdEntity;
import com.backoffice.operations.entity.CivilIdParameter;
import com.backoffice.operations.entity.OtpEntity;
import com.backoffice.operations.entity.User;
import com.backoffice.operations.enums.CardStatus;
import com.backoffice.operations.payloads.CivilIdAPIResponse.CustomerFull;
import com.backoffice.operations.payloads.ExternalApiResponseDTO.Result.Card;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
// import com.backoffice.operations.payloads.ValidationResultDTO;
import com.backoffice.operations.repository.AccessTokenRepository;
import com.backoffice.operations.repository.BlockUnblockActionRepository;
import com.backoffice.operations.repository.CardRepository;
import com.backoffice.operations.repository.CivilIdParameterRepository;
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
    private RestTemplate basicAuthRestTemplate;
    @Value("${external.api.m2p.civilId}")
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
    @Autowired
    private CivilIdParameterRepository civilIdParameterRepository;
    @Autowired
    private AccessTokenRepository accessTokenRepository;
    @Autowired
    @Qualifier("jwtAuth")
    private RestTemplate jwtAuthRestTemplate;
    @Autowired
    private CommonUtils commonUtils;
    @Value("${external.api.sms}")
    private String smsNotify;

    private static CardEntity getCardEntity(Card card, Optional<CivilIdEntity> civilIdEntity,
                                            ResponseEntity<ExternalApiResponseDTO> responseEntity) {
        CardEntity cardEntity = new CardEntity();
        cardEntity.setUniqueKeyCivilId(civilIdEntity.get().getId().toString());
        cardEntity.setCivilId(civilIdEntity.get().getCivilId());
        cardEntity.setCardKitNo(card.getKitNo());
        cardEntity.setExpiry(card.getExpiryDate());
        cardEntity.setDobOfUser(responseEntity.getBody().getResult().getDob());
        return cardEntity;
    }

    @Override
    public GenericResponseDTO<Object> validateCivilId(String entityId, String token) {
        long id = 1;
        CivilIdParameter civilIdParameter = civilIdParameterRepository.findById(id).orElse(null);
        int allowedAttempts = civilIdParameter.getCivilIdMaxAttempts();
        int timeoutSeconds = civilIdParameter.getCivilIdCooldownInSec();

        String userEmail = jwtTokenProvider.getUsername(token);
        Optional<User> user = userRepository.findByEmail(userEmail);
        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
        AccessToken accessToken;
        try {
            // GET Access token from M2P and save it into the DB.
            ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
            logger.info("response: {}", response.getBody());

            accessToken = saveAccessToken(Objects.requireNonNull(response.getBody()));
            logger.info("accessToken: {}", accessToken);

            Optional<CivilIdEntity> civilIdEntityDB = civilIdRepository.findByEntityId(entityId);
            if (civilIdEntityDB.isPresent()) {
                if (StringUtils.hasLength(civilIdEntityDB.get().getCivilId())) {
                    Map<String, String> data = new HashMap<>();
                    responseDTO.setStatus("Success");
                    responseDTO.setMessage("Success");
                    data.put("uniqueKey", civilIdEntityDB.get().getId());
                    responseDTO.setData(data);
                    return responseDTO;
                } else if (civilIdEntityDB.get().getAttempts() < allowedAttempts) {
                    // Valid attempt, update the entity
                    civilIdEntityDB.get().setAttempts(civilIdEntityDB.get().getAttempts() + 1);
                    civilIdEntityDB.get().setLastAttemptTime(LocalDateTime.now());

                    civilIdRepository.save(civilIdEntityDB.get());
                    Map<String, String> data = new HashMap<>();
                    responseDTO.setStatus("Failure");
                    responseDTO.setMessage("Something went wrong");
                    data.put("uniqueKey", civilIdEntityDB.get().getId().toString());
                    responseDTO.setData(data);
                    return responseDTO;

                } else if (ChronoUnit.SECONDS.between(civilIdEntityDB.get().getLastAttemptTime(),
                        LocalDateTime.now()) < timeoutSeconds) {
                    logger.error("Session Is Blocked");
                    Map<String, String> data = new HashMap<>();
                    responseDTO.setStatus("Failure");
                    responseDTO.setMessage("Session Is Blocked");
                    data.put("uniqueKey", civilIdEntityDB.get().getId());
                    responseDTO.setData(data);
                    return responseDTO;
                } else if (civilIdEntityDB.get().getAttempts() < allowedAttempts) {
                    logger.error("Civil Id Blocked, Too many attempts or timeout exceeded");
                    Map<String, String> data = new HashMap<>();
                    responseDTO.setStatus("Failure");
                    responseDTO.setMessage("Civil Id Blocked");
                    data.put("uniqueKey", civilIdEntityDB.get().getId());
                    responseDTO.setData(data);
                    return responseDTO;
                } else {
                    logger.error("Device Blocked");
                    Map<String, String> data = new HashMap<>();
                    responseDTO.setStatus("Failure");
                    responseDTO.setMessage("Device Blocked");
                    data.put("uniqueKey", civilIdEntityDB.get().getId());
                    responseDTO.setData(data);
                    return responseDTO;
                }
            } else {
                CivilIdEntity civilIdEntity = new CivilIdEntity();
                civilIdEntity.setEntityId(entityId);
                civilIdEntity.setUserId(user.get().getId());
                try {
                    if (user.isPresent()) {

                        ResponseEntity<CivilIdAPIResponse> responseEntity = null;
                        if (Objects.nonNull(accessToken)) {
                            HttpHeaders headers = new HttpHeaders();
                            headers.setBearerAuth(accessToken.getAccessToken());
                            HttpEntity<String> entity = new HttpEntity<>(headers);
                            String apiUrl = civilIdExternalAPI + entityId;
                            responseEntity = jwtAuthRestTemplate.exchange(apiUrl, HttpMethod.GET, entity,
                                    CivilIdAPIResponse.class);
                        }

                        CivilIdAPIResponse apiResponse = responseEntity.getBody();
                        if (apiResponse != null && apiResponse.isSuccess()) {
                            CustomerFull customerFull = apiResponse.getResponse().getResult().getCustomerFull();

                            if (customerFull != null) {
                                civilIdEntity.setCivilId(customerFull.getCustNo());
                                civilIdRepository.save(civilIdEntity);
                                Map<String, String> data = new HashMap<>();
                                responseDTO.setStatus("Success");
                                responseDTO.setMessage("Success");
                                data.put("uniqueKey", civilIdEntity.getId());
                                responseDTO.setData(data);
                                return responseDTO;
                            }
                        }
                    }
                    civilIdRepository.save(civilIdEntity);
                    Map<String, String> data = new HashMap<>();
                    responseDTO.setMessage("Failure");
                    responseDTO.setStatus("Something went wrong");
                    data.put("uniqueKey", civilIdEntity.getId());
                    responseDTO.setData(data);
                    return responseDTO;

                } catch (Exception e) {
                    civilIdRepository.save(civilIdEntity);

                    logger.error("ERROR in class CivilIdServiceImpl method validateCivilId", e);
                    Map<String, String> data = new HashMap<>();
                    responseDTO.setStatus("Failure");
                    responseDTO.setMessage("Something went wrong");
                    data.put("uniqueKey", civilIdEntity.getId());
                    responseDTO.setData(data);
                    return responseDTO;
                }
            }
        } catch (Exception e) {
            logger.error("Error in calling token API : ", e);
            Map<String, String> data = new HashMap<>();
            data.put("uniqueKey", null);
            responseDTO.setStatus("Failure");
            responseDTO.setMessage("Something went wrong");
            responseDTO.setData(data);
            return responseDTO;
        }
    }

    public ResponseEntity<AccessTokenResponse> getToken() {
        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Set request body parameters
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "client_credentials");
        requestBody.add("scope", "openid profile email");
        requestBody.add("client_id", "mpp-digital-app");
        requestBody.add("client_secret", "gh2KpMeNlwuSZJBQqCyhRbnRh0BHQwCW");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        logger.info("requestUrl: {}", tokenApiUrl);
        logger.info("requestEntity: {}", requestEntity);
        ResponseEntity<AccessTokenResponse> response = jwtAuthRestTemplate.exchange(tokenApiUrl, HttpMethod.POST, requestEntity, AccessTokenResponse.class);
        logger.info("response: {}", response);
        return response;
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
    public GenericResponseDTO<Object> verifyCard(EntityIdDTO entityIdDTO, String token) {
        logger.debug("In class CivilIdServiceImpl method getCardList");
        String userEmail = jwtTokenProvider.getUsername(token);
        Optional<User> user = userRepository.findByEmail(userEmail);
        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
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
                    ResponseEntity<ExternalApiResponseDTO> responseEntity = basicAuthRestTemplate.exchange(apiUrl,
                            HttpMethod.POST, requestEntity, ExternalApiResponseDTO.class);
                    if (Objects.requireNonNull(responseEntity.getBody()).getResult() != null) {
                        List<Card> cardList = responseEntity.getBody().getResult().getCardList();
                        for (Card card : cardList) {
                            String actualFirstFourDigits = card.getCardNo().substring(0, 4);
                            String actualLastFourDigits = card.getCardNo().substring(card.getCardNo().length() - 4);
                            if (entityIdDTO.getFirstFourDigitscardNo().equals(actualFirstFourDigits)
                                    && entityIdDTO.getLastFourDigitscardNo().equals(actualLastFourDigits)) {
                                if (card.getStatus().equalsIgnoreCase(CardStatus.LOCKED.name())) {
                                    Map<String, String> data = new HashMap<>();
                                    responseDTO.setStatus("Success");
                                    responseDTO.setMessage("Your card is locked");
                                    data.put("uniqueKey", entityIdDTO.getUniqueKey());
                                    responseDTO.setData(data);
                                    return responseDTO;
                                } else if (card.getStatus().equalsIgnoreCase(CardStatus.BLOCKED.name())) {
                                    Map<String, String> data = new HashMap<>();
                                    responseDTO.setStatus("Failure");
                                    responseDTO.setMessage("Your card is permanently blocked");
                                    data.put("uniqueKey", entityIdDTO.getUniqueKey());
                                    responseDTO.setData(data);
                                    return responseDTO;
                                } else if (card.getStatus().equalsIgnoreCase(CardStatus.ALLOCATED.name())) {
                                    //send OTP
                                    sendOtp(civilIdEntity, responseDTO);
                                    CardEntity cardEntity = getCardEntity(card, civilIdEntity, responseEntity);
                                    cardRepository.save(cardEntity);
                                    Map<String, String> data = new HashMap<>();
                                    responseDTO.setStatus("Success");
                                    responseDTO.setMessage("Success");
                                    data.put("uniqueKey", entityIdDTO.getUniqueKey());
                                    responseDTO.setData(data);
                                    return responseDTO;
                                } else {
                                    Map<String, String> data = new HashMap<>();
                                    responseDTO.setStatus("Failure");
                                    responseDTO.setMessage("Something went wrong");
                                    data.put("uniqueKey", entityIdDTO.getUniqueKey());
                                    responseDTO.setData(data);
                                    return responseDTO;
                                }
                            } else {
                                Map<String, String> data = new HashMap<>();
                                responseDTO.setStatus("Failure");
                                responseDTO.setMessage("Please verify card first/last four digits.");
                                data.put("uniqueKey", entityIdDTO.getUniqueKey());
                                responseDTO.setData(data);
                                return responseDTO;
                            }
                        }
                    }
                } else {
                    Map<String, String> data = new HashMap<>();
                    responseDTO.setStatus("Failure");
                    responseDTO.setMessage("Something went wrong");
                    data.put("uniqueKey", entityIdDTO.getUniqueKey());
                    responseDTO.setData(data);
                    return responseDTO;
                }
            }
            Map<String, String> data = new HashMap<>();
            responseDTO.setStatus("Failure");
            responseDTO.setMessage("Something went wrong");
            data.put("uniqueKey", entityIdDTO.getUniqueKey());
            responseDTO.setData(data);
            return responseDTO;
        } catch (Exception e) {
            logger.error("ERROR in class CivilIdServiceImpl method verifyCard", e);
            Map<String, String> data = new HashMap<>();
            responseDTO.setStatus("Failure");
            responseDTO.setMessage("Something went wrong");
            data.put("uniqueKey", entityIdDTO.getUniqueKey());
            responseDTO.setData(data);
            return responseDTO;
        }
    }

    public GenericResponseDTO<Object> sendOtp(Optional<CivilIdEntity> civilIdEntity, GenericResponseDTO<Object> responseDTO) {
        OtpEntity otpEntity = otpRepository
                .findByUniqueKeyCivilId(civilIdEntity.get().getId());
        try {
            if (Objects.isNull(otpEntity)) {
                otpEntity = new OtpEntity();
                // Set uniqueid against civil id.
                otpEntity.setUniqueKeyCivilId(civilIdEntity.get().getId());

//										String newOtp = CommonUtils.generateRandomOtp();
                String newOtp = "1234";
                otpEntity.setOtp(newOtp);
                otpEntity.setLastAttemptTime(LocalDateTime.now());
                otpRepository.save(otpEntity);

                Map<String, String> data = new HashMap<>();
                responseDTO.setStatus("Success");
                responseDTO.setMessage("Success");
                data.put("uniqueKey", civilIdEntity.get().getId());
                responseDTO.setData(data);
                return responseDTO;
//                return getSMSResponseObject(newOtp, responseDTO, civilIdEntity);
            }
        } catch (Exception e) {
            logger.error("ERROR in class CivilIdServiceImpl method sendOtp", e);
            Map<String, String> data = new HashMap<>();
            responseDTO.setStatus("Failure");
            responseDTO.setMessage("Something went wrong");
            data.put("uniqueKey", civilIdEntity.get().getId());
            responseDTO.setData(data);
            return responseDTO;
        }
        return responseDTO;
    }

    private GenericResponseDTO<Object> getSMSResponseObject(String newOtp, GenericResponseDTO<Object> responseDTO, Optional<CivilIdEntity> civilIdEntity) {
        try {
            // GET Access token from M2P and save it into the DB.
            ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
            logger.info("response: {}", response.getBody());
            if (Objects.nonNull(response.getBody())) {

                SMSNotifyRequest smsRequest = new SMSNotifyRequest();
                smsRequest.setMessage("Your new OTP is: " + newOtp);
                smsRequest.setLanguage(0);
//			smsRequest.setRecipients(request.getRecipientNumbers());
                smsRequest.setLocal(true);

                HttpHeaders smsHeaders = new HttpHeaders();
                smsHeaders.setContentType(MediaType.APPLICATION_JSON);
                smsHeaders.setBearerAuth(response.getBody().getAccessToken());
                HttpEntity<SMSNotifyRequest> httpEntity = new HttpEntity<>(smsRequest, smsHeaders);

                ResponseEntity<Object> smsResponseEntity = jwtAuthRestTemplate.exchange(smsNotify, HttpMethod.POST, httpEntity, Object.class);
                if (Objects.nonNull(smsResponseEntity.getBody())) {
                    Map<String, String> data = new HashMap<>();
                    responseDTO.setStatus("Success");
                    responseDTO.setMessage("Success");
                    data.put("uniqueKey", civilIdEntity.get().getId());
                    responseDTO.setData(data);
                    return responseDTO;
                }
            }
            Map<String, String> data = new HashMap<>();
            responseDTO.setStatus("Success");
            responseDTO.setMessage("Success");
            data.put("uniqueKey", civilIdEntity.get().getId());
            responseDTO.setData(data);
            return responseDTO;
        } catch (Exception e) {
            logger.error("ERROR in class CivilIdServiceImpl method getSMSResponseObject", e);
            Map<String, String> data = new HashMap<>();
            responseDTO.setStatus("Failure");
            responseDTO.setMessage("Something went wrong");
            data.put("uniqueKey", civilIdEntity.get().getId());
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
            ResponseEntity<Object> responseEntity = basicAuthRestTemplate.exchange(fetchAllCustomers, HttpMethod.POST,
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
        ResponseEntity<Object> response = basicAuthRestTemplate.postForEntity(blockUnblockURL, requestEntity, Object.class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            // TODO: log proper exception and map it accordingly.
            return response.getBody();
        }
    }
}