package com.backoffice.operations.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

import com.backoffice.operations.entity.CardEntity;
import com.backoffice.operations.entity.TransactionLimitsEntity;
import com.backoffice.operations.entity.TransactionMaxMinLimitsParameter;
import com.backoffice.operations.entity.User;
import com.backoffice.operations.payloads.TransactionLimitsDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.CardRepository;
import com.backoffice.operations.repository.TransactionLimitsRepository;
import com.backoffice.operations.repository.TransactionMaxMinLimitsParameterRepo;
import com.backoffice.operations.repository.UserRepository;
import com.backoffice.operations.security.JwtTokenProvider;
import com.backoffice.operations.service.TransactionLimitService;

@Service
public class TransactionLimitServiceImpl implements TransactionLimitService {

	@Value("${external.api.setPreferenceUrl}")
	private String setPreferenceUrl;
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

	@Override
	public GenericResponseDTO<Object> setTransactionLimits(TransactionLimitsDTO transactionLimitsDTO, String token) {

		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		String userEmail = jwtTokenProvider.getUsername(token);
		Optional<User> user = userRepository.findByEmail(userEmail);

		if (user.isPresent()) {

			CardEntity cardEntity = cardRepository.findByUniqueKeyCivilId(transactionLimitsDTO.getUniqueKey());

			TransactionLimitsEntity transactionLimitsEntity = new TransactionLimitsEntity();
			TransactionLimitsEntity newTransactionLimitsEntity = transactionLimitsRepository
					.findByUniqueKey(transactionLimitsDTO.getUniqueKey());

			long id = 1;
			TransactionMaxMinLimitsParameter transactionMaxMinLimitsParameter = transactionMaxLimitsParameterRepo
					.findById(id).orElse(null);
			double merchantOutletMaxLimits = transactionMaxMinLimitsParameter.getMerchantOutletMaxLimits();
			double merchantOutletMinLimits = transactionMaxMinLimitsParameter.getMerchantOutletMinLimits();
			double onlineShoppingMaxLimits = transactionMaxMinLimitsParameter.getOnlineShoppingMaxLimits();
			double onlineShoppingMinLimits = transactionMaxMinLimitsParameter.getOnlineShoppingMinLimits();
			double atmWithdrawalMaxLimits = transactionMaxMinLimitsParameter.getAtmWithdrawalMaxLimits();
			double atmWithdrawalMinLimits = transactionMaxMinLimitsParameter.getAtmWithdrawalMinLimits();
			double tapAndPayMaxLimits = transactionMaxMinLimitsParameter.getTapAndPayMaxLimits();
			double tapAndPayMinLimits = transactionMaxMinLimitsParameter.getTapAndPayMinLimits();

			if ((transactionLimitsDTO.getMerchantOutletLimits() > merchantOutletMaxLimits)
					|| (transactionLimitsDTO.getOnlineShoppingLimits() > onlineShoppingMaxLimits)
					|| (transactionLimitsDTO.getAtmWithdrawalLimits() > atmWithdrawalMaxLimits)
					|| (transactionLimitsDTO.getTapAndPayLimits() > tapAndPayMaxLimits)) {
				responseDTO.setStatus("Failure");
				responseDTO.setMessage("Transaction limits should not exceed 100000 OMR.");
				return responseDTO;
			}
			if ((transactionLimitsDTO.getMerchantOutletLimits() < merchantOutletMinLimits)
					|| (transactionLimitsDTO.getOnlineShoppingLimits() < onlineShoppingMinLimits)
					|| (transactionLimitsDTO.getAtmWithdrawalLimits() < atmWithdrawalMinLimits)
					|| (transactionLimitsDTO.getTapAndPayLimits() < tapAndPayMinLimits)) {
				responseDTO.setStatus("Failure");
				responseDTO.setMessage("Transaction limits should not below 1 OMR.");
				return responseDTO;
			}

			if (((transactionLimitsDTO.getMerchantOutletLimits() >= merchantOutletMinLimits)
					&& (transactionLimitsDTO.getMerchantOutletLimits() <= merchantOutletMaxLimits))
					|| ((transactionLimitsDTO.getOnlineShoppingLimits() >= onlineShoppingMinLimits)
							&& (transactionLimitsDTO.getOnlineShoppingLimits() <= onlineShoppingMaxLimits))
					|| ((transactionLimitsDTO.getAtmWithdrawalLimits() >= atmWithdrawalMinLimits)
							&& (transactionLimitsDTO.getAtmWithdrawalLimits() <= atmWithdrawalMaxLimits))
					|| ((transactionLimitsDTO.getTapAndPayLimits() >= tapAndPayMinLimits)
							&& (transactionLimitsDTO.getTapAndPayLimits() <= tapAndPayMaxLimits))) {

				HttpHeaders headers = new HttpHeaders();
				headers.add("TENANT", "ALIZZ_UAT");
				headers.setContentType(MediaType.APPLICATION_JSON);
				String requestBody = "{\r\n    \"entityId\": \"" + cardEntity.getCivilId()
						+ "\",\r\n    \"limitConfigs\": [\r\n        {\r\n            \"channel\": \"POS\",\r\n            \"maxAmount\": "
						+ transactionLimitsDTO.getMerchantOutletLimits()
						+ "\r\n       },\r\n        {\r\n            \"channel\": \"ECOM\",\r\n            \"maxAmount\": "
						+ transactionLimitsDTO.getOnlineShoppingLimits()
						+ "\r\n        },\r\n        {\r\n            \"channel\": \"ATM\",\r\n            \"maxAmount\": "
						+ transactionLimitsDTO.getAtmWithdrawalLimits()
						+ "\r\n        },\r\n        {\r\n            \"channel\": \"CONTACTLESS\",\r\n            \"maxAmount\": "
						+ transactionLimitsDTO.getTapAndPayLimits() + "\r\n        }\r\n    ]\r\n}";
				HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
				ResponseEntity<Object> responseEntity = basicAuthRestTemplate.exchange(setPreferenceUrl,
						HttpMethod.POST, requestEntity, Object.class);

				if ((responseEntity.getStatusCode() == HttpStatus.OK) && (responseEntity.getBody() != null)) {
					
					if (Objects.nonNull(newTransactionLimitsEntity)) {
						newTransactionLimitsEntity
								.setMerchantOutletLimits(transactionLimitsDTO.getMerchantOutletLimits());
						newTransactionLimitsEntity
								.setOnlineShoppingLimits(transactionLimitsDTO.getOnlineShoppingLimits());
						newTransactionLimitsEntity
								.setAtmWithdrawalLimits(transactionLimitsDTO.getAtmWithdrawalLimits());
						newTransactionLimitsEntity.setTapAndPayLimits(transactionLimitsDTO.getTapAndPayLimits());
						newTransactionLimitsEntity.setDateModified(LocalDateTime.now());

						transactionLimitsRepository.save(newTransactionLimitsEntity);
						Map<String, Object> data = new HashMap<>();
						responseDTO.setStatus("Success");
						responseDTO.setMessage("Transaction limits is set!");
						data.put("Transaction limits", newTransactionLimitsEntity);
						responseDTO.setData(data);
					} else {
						transactionLimitsEntity.setUniqueKey(transactionLimitsDTO.getUniqueKey());
						transactionLimitsEntity.setMerchantOutletLimits(transactionLimitsDTO.getMerchantOutletLimits());
						transactionLimitsEntity.setOnlineShoppingLimits(transactionLimitsDTO.getOnlineShoppingLimits());
						transactionLimitsEntity.setAtmWithdrawalLimits(transactionLimitsDTO.getAtmWithdrawalLimits());
						transactionLimitsEntity.setTapAndPayLimits(transactionLimitsDTO.getTapAndPayLimits());
						transactionLimitsEntity.setDateModified(LocalDateTime.now());

						transactionLimitsRepository.save(transactionLimitsEntity);
						Map<String, Object> data = new HashMap<>();
						responseDTO.setStatus("Success");
						responseDTO.setMessage("Transaction limits is set!");
						data.put("Transaction limits", transactionLimitsEntity);
						responseDTO.setData(data);
					}
					
					return responseDTO;
					
				}
			}
		}
		responseDTO.setStatus("Failure");
		responseDTO.setMessage("Something went wrong");
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
	public GenericResponseDTO<Object> getTransactionLimitsByCustId(String uniqueKey, String token) {

		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		String userEmail = jwtTokenProvider.getUsername(token);
		Optional<User> user = userRepository.findByEmail(userEmail);

		if (user.isPresent()) {
			TransactionLimitsEntity transactionLimitsEntity = transactionLimitsRepository
					.findByUniqueKey(uniqueKey);
			Map<String, Object> data = new HashMap<>();
			responseDTO.setStatus("Success");
			responseDTO.setMessage("Customer transaction limits list!");
			data.put("list", transactionLimitsEntity);
			responseDTO.setData(data);
			return responseDTO;
		}
		responseDTO.setStatus("Failure");
		responseDTO.setMessage("Something went wrong");
		return responseDTO;
	}
}
