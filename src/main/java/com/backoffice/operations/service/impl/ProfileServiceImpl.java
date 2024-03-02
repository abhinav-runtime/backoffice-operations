package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.CivilIdEntity;
import com.backoffice.operations.entity.Profile;
import com.backoffice.operations.entity.User;
import com.backoffice.operations.payloads.AccessTokenResponse;
import com.backoffice.operations.payloads.CivilIdAPIResponse;
import com.backoffice.operations.payloads.UpdateProfileRequest;
import com.backoffice.operations.payloads.UpdateProfileRequestBank;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.CivilIdRepository;
import com.backoffice.operations.repository.ProfileRepository;
import com.backoffice.operations.repository.UserRepository;
import com.backoffice.operations.security.JwtTokenProvider;
import com.backoffice.operations.service.ProfileService;
import com.backoffice.operations.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);

    @Value("${external.api.m2p.civilId}")
    private String civilId;

    @Value("${external.api.profile.update}")
    private String profileUpdate;

    @Value("${external.credit.card.base.url}")
    private String verifyEmailLink;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("jwtAuth")
    private RestTemplate jwtAuthRestTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private CivilIdRepository civilIdRepository;

    @Autowired
    private CivilIdServiceImpl civilIdServiceImpl;

    @Override
    public GenericResponseDTO<Object> getCustomerInfo(String uniqueKey, String nId, String lang, String token) {
        String userEmail = jwtTokenProvider.getUsername(token);
        Optional<User> user = userRepository.findByEmail(userEmail);
        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
        try {
            if (user.isPresent()) {
                ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
                if (Objects.nonNull(response.getBody())) {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setBearerAuth(response.getBody().getAccessToken());
                    HttpEntity<String> entity = new HttpEntity<>(headers);

                    String apiUrl = civilId + nId;
                    ResponseEntity<CivilIdAPIResponse> responseEntity = jwtAuthRestTemplate.exchange(apiUrl, HttpMethod.GET, entity,
                            CivilIdAPIResponse.class);

                    CivilIdAPIResponse apiResponse = responseEntity.getBody();
                    if (apiResponse != null && apiResponse.isSuccess()) {
                        CivilIdAPIResponse.CustomerFull customerFull = apiResponse.getResponse().getResult().getCustomerFull();

                        if (customerFull != null) {
                            Profile profile = new Profile();
                            Map<String, Object> data = new HashMap<>();
                            if (Objects.nonNull(customerFull.getCustpersonal())) {
                                CivilIdAPIResponse.CustPersonalDTO custPersonalDTO = customerFull.getCustpersonal();
                                profile.setEmailId(custPersonalDTO.getEmailid());
                                profile.setMobNum(custPersonalDTO.getMobnum());
                                profile.setNId(nId);
                                profile.setMobisdno(custPersonalDTO.getMobisdno());

                                data.put("emailId", custPersonalDTO.getEmailid());
                                data.put("mobNum", custPersonalDTO.getMobnum());
                                data.put("mobIsdNo", custPersonalDTO.getMobisdno());
                            }
                            profile.setUniqueKeyCivilId(uniqueKey);
                            profile.setFullName(customerFull.getFullname());
                            if (!profileRepository.findByUniqueKeyCivilId(uniqueKey).isPresent()) {
                                profileRepository.save(profile);
                            }
                            responseDTO.setStatus("Success");
                            responseDTO.setMessage("Success");
                            data.put("uniqueKey", uniqueKey);
                            data.put("fullName", customerFull.getFullname());
                            data.put("nId", nId);
                            responseDTO.setData(data);
                            return responseDTO;
                        }
                    }
                }
            }
            Map<String, Object> data = new HashMap<>();
            responseDTO.setStatus("Failure");
            responseDTO.setMessage("Something went wrong");
            data.put("uniqueKey", uniqueKey);
            responseDTO.setData(data);
            return responseDTO;
        } catch (Exception e) {
            logger.error("ERROR in class ProfileServiceImpl method getCustomerInfo", e);
            Map<String, Object> data = new HashMap<>();
            responseDTO.setStatus("Failure");
            responseDTO.setMessage("Something went wrong");
            data.put("uniqueKey", uniqueKey);
            responseDTO.setData(data);
            return responseDTO;
        }
    }

    @Override
    public GenericResponseDTO<Object> updateProfile(String uniqueKey, UpdateProfileRequest updateProfileRequest, String token) {
        String userEmail = jwtTokenProvider.getUsername(token);
        Optional<User> user = userRepository.findByEmail(userEmail);
        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
        try {
            if (user.isPresent()) {
                Map<String, Object> data = new HashMap<>();
                Profile profile = profileRepository.findByUniqueKeyCivilId(uniqueKey).orElseThrow(() -> new RuntimeException("Profile not found"));
                Optional<CivilIdEntity> civilIdEntity = civilIdRepository.findById(uniqueKey);
                if (civilIdEntity.isPresent()) {
                    if (StringUtils.hasLength(updateProfileRequest.getMobileNumber()) &&
                            StringUtils.hasLength(updateProfileRequest.getCivilId()) &&
                            civilIdEntity.get().getEntityId().equalsIgnoreCase(updateProfileRequest.getCivilId())) {
                        profile.setCivilId(updateProfileRequest.getCivilId());
                        profile.setExpiryDate(updateProfileRequest.getExpiryDate());
                        profile.setMobNum(updateProfileRequest.getMobileNumber());

                        GenericResponseDTO<Object> newOtp = civilIdServiceImpl.sendOtp(civilIdEntity, responseDTO);
                        if (Objects.nonNull(newOtp) && newOtp.getStatus().equalsIgnoreCase("Success")) {
                            profile.setUserId(user.get().getId());
                            profile.setEmailStatementFlag(updateProfileRequest.getEmailStatementFlag());
                            profileRepository.save(profile);

                            responseDTO.setStatus("Success");
                            responseDTO.setMessage("Success");
                            data.put("uniqueKey", uniqueKey);
                            responseDTO.setData(data);
                            return responseDTO;
                        }
                    } else if (StringUtils.hasLength(updateProfileRequest.getEmailAddress())) {
                        profile.setEmailId(updateProfileRequest.getEmailAddress());
                        user.get().setEmail(updateProfileRequest.getEmailAddress());
                        String newToken = jwtTokenProvider.generateToken(user.get());
                        sendVerificationEmail(profile.getEmailId(),
                                verifyEmailLink + "backoffice/api/v1//profile/verifyToken?token=" + newToken, user.get().getUsername());

                        profile.setUserId(user.get().getId());
                        profile.setEmailStatementFlag(updateProfileRequest.getEmailStatementFlag());
                        profileRepository.save(profile);
                    } else if (null != updateProfileRequest.getEmailStatementFlag()) {
                        profile.setEmailStatementFlag(updateProfileRequest.getEmailStatementFlag());
                        profileRepository.save(profile);
                    } else {
                        responseDTO.setStatus("Failure");
                        responseDTO.setMessage("Invalid CivilId passed.");
                        data.put("uniqueKey", uniqueKey);
                        responseDTO.setData(data);
                        return responseDTO;
                    }
                }
            }
            Map<String, Object> data = new HashMap<>();
            responseDTO.setStatus("Failure");
            responseDTO.setMessage("Something went wrong");
            data.put("uniqueKey", uniqueKey);
            responseDTO.setData(data);
            return responseDTO;
        } catch (Exception e) {
            logger.error("ERROR in class ProfileServiceImpl method updateProfile", e);
            Map<String, Object> data = new HashMap<>();
            responseDTO.setStatus("Failure");
            responseDTO.setMessage("Something went wrong");
            data.put("uniqueKey", uniqueKey);
            responseDTO.setData(data);
            return responseDTO;
        }
    }

    @Override
    public String verifyEmail(String token) {
        String userEmail = jwtTokenProvider.getUsername(token);
        Optional<User> user = userRepository.findByEmail(userEmail);
        Optional<Profile> profile = profileRepository.findByUserId(user.get().getId());
        if (profile.isPresent()) {
            UpdateProfileRequestBank updateProfileRequestBank = new UpdateProfileRequestBank();
            updateProfileRequestBank.setEmailAddress(profile.get().getEmailId());
            updateProfileRequestBank.setMobileNumber(profile.get().getMobNum());
            ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
            if (Objects.nonNull(response.getBody())) {
                String apiUrl = profileUpdate + profile.get().getNId();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setBearerAuth(response.getBody().getAccessToken());
                HttpEntity<UpdateProfileRequestBank> entity = new HttpEntity<>(updateProfileRequestBank, headers);

                ResponseEntity<Object> responseEntity = jwtAuthRestTemplate.exchange(apiUrl, HttpMethod.PUT, entity, Object.class);
                if (Objects.nonNull(responseEntity.getBody())) {
                    user.get().setEmail(userEmail);
                    userRepository.save(user.get());
                    return "Email verified successfully.";
                }
            }
        }
        return "Email verification failed.";
    }

    public void sendVerificationEmail(String emailAddress, String verificationLink, String username) {
        String htmlMessage = "<html><body>"
                + "<p>Dear " + username + ",</p>"
                + "<p>Please click the following link to verify your email address:</p>"
                + "<p><a href=\"" + verificationLink + "\">Verify Email</a></p>"
                + "<p>Thank you.</p>"
                + "</body></html>";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@gmail.com");
        message.setTo(emailAddress);
        message.setSubject("Email Verification");
        message.setText(htmlMessage);

        emailSender.send(message);
    }
}
