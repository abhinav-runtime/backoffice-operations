package com.backoffice.operations.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.backoffice.operations.entity.Customer;
import com.backoffice.operations.payloads.AccessTokenResponse;
import com.backoffice.operations.payloads.BOCustomerDetailsResponseDTO;
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
		GenericResponseDTO<List<Customer>> customerResponse = new GenericResponseDTO<>();
		Map<String, String> customerData = new HashMap<>();
		try {
			List<Customer> allCustomers = customerRepository.findAll();

			if (allCustomers.size() != 0) {
				customerResponse.setStatus("success");
				customerResponse.setMessage("All customers List");
				customerResponse.setData(allCustomers);
			} else {
				customerResponse.setStatus("Failure");
				customerResponse.setMessage("Something went wrong.");
				customerResponse.setData(null);
			}
		} catch (Exception e) {
			customerResponse.setStatus("Failure");
			customerResponse.setMessage("Something went wrong.");
			customerResponse.setData(null);
			logger.error("Error: {}", e.getMessage());
		}

		return customerResponse;
	}

	@Override
	public GenericResponseDTO<CustomerResponseDTO> editCustomer(String custId, CustomerRequestDTO customerRequestDTO) {
		GenericResponseDTO<CustomerResponseDTO> response = new GenericResponseDTO<>();
		try {
			Customer customer = customerRepository.findByCustId(custId);
			if (customer != null) {
				if (customerRequestDTO.getName() != null && customerRequestDTO.getName() != "") {
					customer.setName(customerRequestDTO.getName());
				}
				if (customerRequestDTO.getBranch() != null && customerRequestDTO.getBranch() != "") {
					customer.setBranch(customerRequestDTO.getBranch());
				}
				if (customerRequestDTO.getCountry() != null && customerRequestDTO.getCountry() != "") {
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
		} catch (Exception e) {
			logger.error("Error: {}", e.getMessage());
			response.setStatus("Failure");
			response.setMessage("Something went wrong.");
			response.setData(null);
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
			ResponseEntity<String> responseEntity = jwtAuthRestTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity,
					String.class);
//			ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity,
//					String.class);

			String jsonResponse = responseEntity.getBody();
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(jsonResponse);
			String firstName = jsonNode.at("/response/result/customerFull/custpersonal/fstname").asText();
			String lastName = jsonNode.at("/response/result/customerFull/custpersonal/lstname").asText();
			String branch = jsonNode.at("/response/result/customerFull/custaccdet/reverseRelationship/0/branch")
					.asText();
			String country = jsonNode.at("/response/result/customerFull/country").asText();
			String costNoString = jsonNode.at("/response/result/customerFull/custno").asText();

			Customer customer = new Customer();
			customer.setCustId(CivilId);
			customer.setName(firstName + " " + lastName);
			customer.setBranch(branch);
			customer.setCountry(country);
			customer.setDateRegistered(new Date(System.currentTimeMillis()));
			customer.setCustNo(costNoString);
			customerRepository.save(customer);

		} catch (Exception e) {
			logger.error("Error: {}", e.getMessage());
		}

	}

	@Override
	public GenericResponseDTO<Object> getCustomersBySearchValue(String searchValue) {
		searchValue = searchValue.toLowerCase().trim().replaceAll("^\\s+|\\s+$", "");

		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		List<Customer> searchResult = new ArrayList<>();
		if (searchValue == "" || searchValue == null) {
			List<Customer> customersBySearchValue = customerRepository.findAll();
			response.setStatus("Success");
			response.setMessage("Search result");
			response.setData(customersBySearchValue);
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
				accuracyThreshold++;
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

	@Override
	public GenericResponseDTO<Object> getCustomerDetails(String custId) {
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		BOCustomerDetailsResponseDTO customerResponseDTO = new BOCustomerDetailsResponseDTO();
		String accessToken = null;
		try {
//			ResponseEntity<AccessTokenResponse> response = commonUtils.getToken();
//			logger.info("response: {}", response.getBody());
//
//			accessToken = Objects.requireNonNull(response.getBody().getAccessToken());
//			logger.info("accessToken: {}", accessToken);

//			String apiUrl = externalApiUrl + custId;
			String apiUrl = "http://182.18.138.199/chandan/api/v1/customer/nid/" + custId;
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
			customerResponseDTO.setCustNo(jsonNode.at("/response/result/customerFull/custno").asText());
			customerResponseDTO.setCusId(custId);
			customerResponseDTO.setStatus("Active");
			customerResponseDTO.setUname("");
			customerResponseDTO.setName(jsonNode.at("/response/result/customerFull/fullname").asText());
			customerResponseDTO.setDob(jsonNode.at("/response/additional_information/birth_date").asText());
			customerResponseDTO.setNationalId(jsonNode.at("/response/additional_information/id_number").asText());
			customerResponseDTO.setNationalType(jsonNode.at("/response/additional_information/id_type").asText());
			customerResponseDTO
					.setNationalExpiry(jsonNode.at("/response/additional_information/id_expiry_date").asText());
			customerResponseDTO.setNationality(jsonNode.at("/response/additional_information/nationality").asText());
			customerResponseDTO
					.setMotherName(jsonNode.at("/response/result/customerFull/custpersonal/mothermaidnname").asText());
			customerResponseDTO.setGender(jsonNode.at("/response/additional_information/gender").asText());
			customerResponseDTO
					.setMaritalStatus(jsonNode.at("/response/result/customerFull/custdomestic/maritalstat").asText());
			customerResponseDTO.setProfession("");
			customerResponseDTO.setDateRegister(customerRepository.findByCustId(custId).getDateRegistered().toString());
			customerResponseDTO.setBranch(jsonNode.at("/response/additional_information/home_branch").asText());
			customerResponseDTO.setRoles("");
			customerResponseDTO.setSegment(jsonNode.at("/response/result/customerFull/custsegment"));
			customerResponseDTO.setStaff(jsonNode.at("/response/additional_information/is_staff").asText());
			customerResponseDTO.setEstatment("");
			customerResponseDTO.setAccesptedvalueDisclamer("");
			customerResponseDTO.setRegistrantionStatus("");

			customerResponseDTO.setPhone(jsonNode.at("/response/result/customerFull/custpersonal/telephno").asText());
			customerResponseDTO.setMobile(jsonNode.at("/response/additional_information/mobile_number").asText());
			customerResponseDTO.setFax(jsonNode.at("/response/result/customerFull/custpersonal/faxnumber").asText());
			customerResponseDTO.setEmail(jsonNode.at("/response/result/customerFull/custpersonal/emailid").asText());
			customerResponseDTO.setAddress1(jsonNode.at("/response/result/customerFull/custpersonal/add1PC").asText());
			customerResponseDTO.setAddress2(jsonNode.at("/response/result/customerFull/custpersonal/add2PC").asText());
			customerResponseDTO.setAddress3(jsonNode.at("/response/result/customerFull/custpersonal/add3PC").asText());
			customerResponseDTO.setCountry(jsonNode.at("/response/result/customerFull/country").asText());

			responseDTO.setStatus("Success");
			responseDTO.setMessage("Customer details");
			responseDTO.setData(customerResponseDTO);

		} catch (Exception e) {
			logger.error("Error: {}", e.getMessage());
			responseDTO.setStatus("Failure");
			responseDTO.setMessage("Something went wrong.");
			responseDTO.setData(new HashMap<>());
		}
		return responseDTO;
	}
}
