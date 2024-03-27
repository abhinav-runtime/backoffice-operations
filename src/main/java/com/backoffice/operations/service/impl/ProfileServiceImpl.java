package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.CivilIdEntity;
import com.backoffice.operations.entity.Profile;
import com.backoffice.operations.entity.ProfileParameter;
import com.backoffice.operations.entity.User;
import com.backoffice.operations.payloads.AccessTokenResponse;
import com.backoffice.operations.payloads.CivilIdAPIResponse;
import com.backoffice.operations.payloads.UpdateProfileRequest;
import com.backoffice.operations.payloads.UpdateProfileRequestBank;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.CivilIdRepository;
import com.backoffice.operations.repository.ProfileParameterRepo;
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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

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

    @Autowired
    private ProfileParameterRepo profileParameterRepo;

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
                            Optional<Profile> profileDb = profileRepository.findByUniqueKeyCivilId(uniqueKey);
                            if (profileDb.isEmpty()) {
                                profileRepository.save(profile);
                            }
                            responseDTO.setStatus("Success");
                            responseDTO.setMessage("Success");
                            data.put("uniqueKey", uniqueKey);
                            data.put("fullName", customerFull.getFullname());
                            data.put("nId", nId);
                            data.put("emailStatementFlag", profileDb.get().isEmailStatementFlag());
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
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat bankFormatter = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            if (user.isPresent()) {
                Map<String, Object> data = new HashMap<>();
                long id = 1;
                ProfileParameter profileParameter = profileParameterRepo.findById(id).orElse(null);
                Profile profile = profileRepository.findByUniqueKeyCivilId(uniqueKey).orElseThrow(() -> new RuntimeException("Profile not found"));
                Optional<CivilIdEntity> civilIdEntity = civilIdRepository.findById(uniqueKey);
                if (civilIdEntity.isPresent()) {
                    if (StringUtils.hasLength(updateProfileRequest.getMobileNumber()) &&
                            StringUtils.hasLength(updateProfileRequest.getCivilId())
//                            && civilIdEntity.get().getEntityId().equalsIgnoreCase(updateProfileRequest.getCivilId())
                    ) {
                        ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
                        if (Objects.nonNull(response.getBody())) {
                            HttpHeaders headers = new HttpHeaders();
                            headers.setBearerAuth(response.getBody().getAccessToken());
                            HttpEntity<String> entity = new HttpEntity<>(headers);

                            String apiUrl = civilId + profile.getNId();
                            ResponseEntity<CivilIdAPIResponse> responseEntity = jwtAuthRestTemplate.exchange(apiUrl, HttpMethod.GET, entity,
                                    CivilIdAPIResponse.class);

                            CivilIdAPIResponse apiResponse = responseEntity.getBody();
                            if (apiResponse != null && apiResponse.isSuccess()) {
                                CivilIdAPIResponse.AdditionalInformation additionalInformation = apiResponse.getResponse().getAdditionalInformation();
                                logger.info("additionalInformation--- {} ", additionalInformation);
                                if(Objects.nonNull(additionalInformation)){
                                    logger.info("apiResponse.getResponse()--- {} ", apiResponse.getResponse());
                                    String bankDate = bankFormatter.format(additionalInformation.getIdExpiryDate());
                                    String inputDate = formatter.format(updateProfileRequest.getExpiryDate());
                                    logger.info("bankDate--- {} ", bankDate);
                                    logger.info("inputDate --- {} ", inputDate);
                                    Date date1 = formatter.parse(bankDate);
                                    Date date2 = bankFormatter.parse(inputDate);
                                    logger.info("date1--- {} ", date1);
                                    logger.info("date2 --- {} ", date2);
                                    if (date1.equals(date2)) {
                                        profile.setCivilId(updateProfileRequest.getCivilId());
                                        profile.setExpiryDate(updateProfileRequest.getExpiryDate());
                                        if (Objects.nonNull(profile.getMobileChangeAttemptLimit()) &&
                                                profile.getMobileChangeAttemptLimit() <= profileParameter.getMobileChangeAttemptLimit()) {
                                            profile.setMobNum(updateProfileRequest.getMobileNumber());
                                            profile.setMobileChangeLockoutDuration(LocalDateTime.now());
                                            profile.setMobileNoChangedLockoutDaysDuration(LocalDate.now());
                                        } else {
                                            if (ChronoUnit.SECONDS.between(profile.getMobileChangeLockoutDuration(),
                                                    LocalDateTime.now()) < profileParameter.getMobileChangeLockoutDurationInSec()) {
                                                responseDTO.setStatus("Success");
                                                responseDTO.setMessage("Session Blocked after exceeding attempt limit");
                                                data.put("uniqueKey", uniqueKey);
                                                data.put("message", "Session Blocked after exceeding attempt limit");
                                                responseDTO.setData(data);
                                                return responseDTO;
                                            } else {
                                                responseDTO.setStatus("Success");
                                                responseDTO.setMessage("Maximum number of mobile number change attempts allowed in a day Exceeded.");
                                                data.put("uniqueKey", uniqueKey);
                                                data.put("message", "Maximum number of mobile number change attempts allowed in a day Exceeded.");
                                                responseDTO.setData(data);
                                                return responseDTO;
                                            }
                                        }
                                        profileRepository.save(profile);
//                        GenericResponseDTO<Object> newOtp =
                                        civilIdServiceImpl.sendOtp(civilIdEntity, responseDTO);
                                        responseDTO.setStatus("Success");
                                        responseDTO.setMessage("Success");
                                    } else {
                                        responseDTO.setStatus("Failure");
                                        responseDTO.setMessage("Invalid Expiry Date.");
                                    }
                                }
                                data.put("uniqueKey", uniqueKey);
                                responseDTO.setData(data);
                                return responseDTO;
                            }
                        }
//                        if (Objects.nonNull(newOtp) && newOtp.getStatus().equalsIgnoreCase("Success")) {
//                            profile.setUserId(user.get().getId());
//                            profile.setEmailStatementFlag(updateProfileRequest.getEmailStatementFlag());
//                            profileRepository.save(profile);
//
//                            responseDTO.setStatus("Success");
//                            responseDTO.setMessage("Success");
//                            data.put("uniqueKey", uniqueKey);
//                            responseDTO.setData(data);
//                            return responseDTO;
//                        }
                    } else if (StringUtils.hasLength(updateProfileRequest.getEmailAddress())) {

                        if (Objects.nonNull(profile.getEmailIdChangeAttemptLimit()) &&
                                profile.getEmailIdChangeAttemptLimit() <= profileParameter.getEmailIdChangeAttemptLimit()) {
                            profile.setEmailId(updateProfileRequest.getEmailAddress());
                            profile.setEmailIdChangeLockoutDuration(LocalDateTime.now());
                            profile.setEmailIdChangedLockoutDaysDuration(LocalDate.now());
                        }else {
                            if (ChronoUnit.SECONDS.between(profile.getEmailIdChangeLockoutDuration(),
                                    LocalDateTime.now()) < profileParameter.getEmailIdChangeLockoutDurationInSec()){
                                responseDTO.setStatus("Success");
                                responseDTO.setMessage("Session Blocked after exceeding Email Id attempt limit");
                                data.put("uniqueKey", uniqueKey);
                                data.put("message", "Session Blocked after exceeding Email Id attempt limit");
                                responseDTO.setData(data);
                                return responseDTO;
                            }else {
                                responseDTO.setStatus("Success");
                                responseDTO.setMessage("Maximum number of Email ID change attempts allowed in a day Exceeded.");
                                data.put("uniqueKey", uniqueKey);
                                data.put("message", "Maximum number of Email ID change attempts allowed in a day Exceeded.");
                                responseDTO.setData(data);
                                return responseDTO;
                            }
                        }
                        user.get().setEmail(updateProfileRequest.getEmailAddress());
                        String newToken = jwtTokenProvider.generateToken(user.get());
                        sendVerificationEmail(profile.getEmailId(),
                                verifyEmailLink + "backoffice/api/v1/profile/verifyToken?token=" + newToken, user.get().getUsername());
                        int attempts = Objects.nonNull(profile.getEmailIdVerificationLinkResendAttempts()) ? profile.getEmailIdVerificationLinkResendAttempts() : 0;
                        if (attempts <= profileParameter.getEmailIdVerificationLinkResendAttempts()){
                            profile.setEmailIdVerificationLinkResendAttempts(attempts++);
                        }else {
                            responseDTO.setStatus("Success");
                            responseDTO.setMessage("Number of times user can resend the verification link exceeded");
                            data.put("uniqueKey", uniqueKey);
                            data.put("message", "Number of times user can resend the verification link exceeded");
                            responseDTO.setData(data);
                            return responseDTO;
                        }
                        profile.setUserId(user.get().getId());
                        profile.setEmailStatementFlag(updateProfileRequest.getEmailStatementFlag());
                        profileRepository.save(profile);

                        responseDTO.setStatus("Success");
                        responseDTO.setMessage("Success");
                        data.put("uniqueKey", uniqueKey);
                        responseDTO.setData(data);
                        return responseDTO;
                    } else if (null != updateProfileRequest.getEmailStatementFlag()) {
                        if (Objects.nonNull(profile.getEmailStatementEnableDisable()) &&
                                profile.getEmailStatementEnableDisable() <= profileParameter.getEmailStatementFlagAttempts()) {
                            profile.setEmailStatementFlag(updateProfileRequest.getEmailStatementFlag());
                        }else {
                            responseDTO.setStatus("Success");
                            responseDTO.setMessage("Number of attempts to enable / disable email statement in a day exceeded");
                            data.put("uniqueKey", uniqueKey);
                            data.put("message", "Number of attempts to enable / disable email statement in a day exceeded");
                            responseDTO.setData(data);
                            return responseDTO;
                        }
                        profileRepository.save(profile);

                        responseDTO.setStatus("Success");
                        responseDTO.setMessage("Success");
                        data.put("uniqueKey", uniqueKey);
                        responseDTO.setData(data);
                        return responseDTO;
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
        message.setFrom("aditya.runtime@gmail.com");
        message.setTo(emailAddress);
        message.setSubject("Email Verification");
        message.setText(htmlMessage);

        emailSender.send(message);
    }
}
