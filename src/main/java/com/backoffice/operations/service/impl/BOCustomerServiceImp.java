package com.backoffice.operations.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.backoffice.operations.entity.AccessToken;
import com.backoffice.operations.entity.Customer;
import com.backoffice.operations.payloads.AccessTokenResponse;
import com.backoffice.operations.payloads.CustomerRequestDTO;
import com.backoffice.operations.payloads.CustomerResponseDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.CivilIdRepository;
import com.backoffice.operations.repository.CustomerRepository;
import com.backoffice.operations.service.BOCustomerService;
import com.backoffice.operations.utils.CommonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BOCustomerServiceImp implements BOCustomerService {
	private static final Logger logger = LoggerFactory.getLogger(BOCustomerServiceImp.class);

	@Value("${external.api.m2p.civilId}")
	private String externalApiUrl;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private CivilIdRepository civilIdEntityBkRepository;
	@Autowired
	private CommonUtils commonUtils;
	@Autowired
	@Qualifier("jwtAuth")
	private RestTemplate jwtAuthRestTemplate;

	@Override
	public GenericResponseDTO<List<Customer>> getAllCustomers() {

		List<Customer> allCustomers = customerRepository.findAll();
		GenericResponseDTO<List<Customer>> customerResponse = new GenericResponseDTO<>();
		if (allCustomers.size() != 0) {
			customerResponse.setStatus("success");
			customerResponse.setMessage("All customers List");
			customerResponse.setData(allCustomers);
		} else {
			customerResponse.setStatus("Failure");
			customerResponse.setMessage("Something went wrong.");
			customerResponse.setData(null);
		}

		return customerResponse;
	}

	@Override
	public GenericResponseDTO<CustomerResponseDTO> editCustomer(String custId, CustomerRequestDTO customerRequestDTO) {
		Customer customer = customerRepository.findByCustId(custId);
		GenericResponseDTO<CustomerResponseDTO> response = new GenericResponseDTO<>();
		if (customer != null) {
			if (customerRequestDTO.getName() != null) {
				customer.setName(customerRequestDTO.getName());
			}
			if (customerRequestDTO.getBranch() != null) {
				customer.setBranch(customerRequestDTO.getBranch());
			}
			if (customerRequestDTO.getCountry() != null) {
				customer.setCountry(customerRequestDTO.getCountry());
			}
			customerRepository.save(customer);
			CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();
			customerResponseDTO.setCustId(customer.getCustId());
			customerResponseDTO.setName(customer.getName());
			customerResponseDTO.setBranch(customer.getBranch());
			customerResponseDTO.setCountry(customer.getCountry());
			customerResponseDTO.setDateRegistered(customer.getDateRegistered());

			response.setStatus("success");
			response.setMessage("Customer updated successfully");
			response.setData(customerResponseDTO);
		} else {
			CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();
			customerResponseDTO.setCustId(custId);
			customerResponseDTO.setName(null);
			customerResponseDTO.setBranch(null);
			customerResponseDTO.setCountry(null);
			customerResponseDTO.setDateRegistered(null);

			response.setStatus("Failure");
			response.setMessage("Something went wrong.");
			response.setData(customerResponseDTO);
		}
		return response;
	}

	@Override
	public void addOnboardCustomer(String uniqueKey) {
		String accessToken = null;
		try {
			ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
			logger.info("response: {}", response.getBody());

			accessToken = Objects.requireNonNull(response.getBody().getAccessToken());
			logger.info("accessToken: {}", accessToken);

			String CivilId = civilIdEntityBkRepository.findById(uniqueKey).get().getEntityId();
			String apiUrl = externalApiUrl + CivilId;

			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(accessToken);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<>(headers);
//			ResponseEntity<String> responseEntity = jwtAuthRestTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity,
//					String.class);
			ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity,
					String.class);

			String jsonResponse = responseEntity.getBody();
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(jsonResponse);
			String firstName = jsonNode.at("/response/result/customerFull/custpersonal/fstname").asText();
			String lastName = jsonNode.at("/response/result/customerFull/custpersonal/lstname").asText();
			String branch = jsonNode.at("/response/result/customerFull/custaccdet/reverseRelationship/0/branch")
					.asText();
			String country = jsonNode.at("/response/result/customerFull/country").asText();

			Customer customer = new Customer();
			customer.setCustId(CivilId);
			customer.setName(firstName + " " + lastName);
			customer.setBranch(branch);
			customer.setCountry(country);
			customer.setDateRegistered(new Date(System.currentTimeMillis()));
			customerRepository.save(customer);

		} catch (Exception e) {
			logger.error("Error: {}", e.getMessage());
		}

	}

	@Override
	public GenericResponseDTO<Object> getCustomersBySearchValue(String searchValue) {
		searchValue = searchValue.toLowerCase();

		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		List<Customer> searchResult = new ArrayList<>();
		if (searchValue == "") {
			response.setStatus("Failure");
			response.setMessage("Search value is empty");
			response.setData(null);
		} else {
			List<Customer> customersBySearchValue = customerRepository.findAll();
			int accuracyThreshold = 1;
			for (Customer customer : customersBySearchValue) {
				if (customer.getName().toLowerCase().contains(searchValue)
						|| customer.getBranch().toLowerCase().contains(searchValue)
						|| customer.getCountry().toLowerCase().contains(searchValue)
						|| customer.getCustId().toLowerCase().contains(searchValue)) {
					searchResult.add(customer);
				}
			}
			
			while (searchResult.size() == 0 && accuracyThreshold <= 5) {
				searchResult = findCustomers(searchValue, accuracyThreshold, customersBySearchValue);
				accuracyThreshold ++;
			}
			
			if (searchResult.size() != 0) {
				response.setStatus("Success");
				response.setMessage("Search result");
				response.setData(searchResult);
			} else {
				response.setStatus("Failure");
				response.setMessage("Something went wrong.");
				response.setData(null);
			}
		}
		return response;
	}

	private List<Customer> findCustomers(String value, int threshold, List<Customer> customersBySearchValue) {
		List<Customer> searchResult = new ArrayList<>();
		for (Customer customer : customersBySearchValue) {
			if (isSimilar(customer.getName().toLowerCase(), value, threshold)
					|| isSimilar(customer.getBranch().toLowerCase(), value, threshold)
					|| isSimilar(customer.getCountry().toLowerCase(), value, threshold)
					|| isSimilar(customer.getCustId().toLowerCase(), value, threshold)) {
				searchResult.add(customer);
			}
		}
		return searchResult;
	}

	private boolean isSimilar(String s1, String s2, int threshold) {
		LevenshteinDistance distance = LevenshteinDistance.getDefaultInstance();
		int dist = distance.apply(s1, s2);
		return dist <= threshold;
	}
}
