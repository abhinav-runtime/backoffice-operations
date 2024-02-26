package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.Profile;
import com.backoffice.operations.entity.User;
import com.backoffice.operations.payloads.AccessTokenResponse;
import com.backoffice.operations.payloads.CivilIdAPIResponse;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.ProfileRepository;
import com.backoffice.operations.repository.UserRepository;
import com.backoffice.operations.security.JwtTokenProvider;
import com.backoffice.operations.service.ProfileService;
import com.backoffice.operations.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class ProfileServiceImpl implements ProfileService {

    @Value("${external.api.m2p.civilId}")
    private String civilId;

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
            Map<String, Object> data = new HashMap<>();
            responseDTO.setStatus("Failure");
            responseDTO.setMessage("Something went wrong");
            data.put("uniqueKey", uniqueKey);
            responseDTO.setData(data);
            return responseDTO;
        }
    }
}
