package com.backoffice.operations.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.entity.Customer;
import com.backoffice.operations.payloads.CustomerRequestDTO;
import com.backoffice.operations.payloads.CustomerResponseDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.security.BOUserToken;
//import com.backoffice.operations.security.BOUserToken;
import com.backoffice.operations.service.BOCustomerService;
import com.backoffice.operations.service.impl.BoAccessHelper;

@RestController
@RequestMapping("/bo/v1/customers")
public class BOCustomerController {
	private static final Logger logger = LoggerFactory.getLogger(BOCustomerController.class);
	@Autowired
	private BOCustomerService customerService;
	@Autowired
	private BOUserToken boUserToken;
	@Autowired
	private BoAccessHelper accessHelper;

	@GetMapping("/all")
	public ResponseEntity<Object> getAllCustomers() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			if (boUserToken.getRolesFromToken().isEmpty()) {
				response.setMessage("Something went wrong.");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
			}
			if (accessHelper.isAccessible("CUSTOMERS_INDIVIDUAL", "VIEW")
					|| accessHelper.isAccessible("CUSTOMERS_INDIVIDUAL", "EDIT")) {

				GenericResponseDTO<List<Customer>> responseDTO = customerService.getAllCustomers();
				if (responseDTO.getStatus().equals("Failure")) {
					return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
				}
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			} else {
				response.setMessage("Something went wrong.");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
			}
		} catch (Exception e) {
			logger.error("Error: {} ", e.getMessage());
			response.setMessage("Something went wrong");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
		}
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/edit/{custId}")
	public ResponseEntity<Object> editCustomer(@PathVariable(value = "custId") String custId, @RequestBody CustomerRequestDTO customer) {
		logger.info("Edit customer id: {} ", custId);
		logger.info("Customer data: {} ", customer);
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			if (boUserToken.getRolesFromToken().isEmpty()) {
				response.setMessage("Something went wrong.");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
			}
			if (accessHelper.isAccessible("CUSTOMERS_INDIVIDUAL", "EDIT")) {
				GenericResponseDTO<CustomerResponseDTO> responseDTO = customerService.editCustomer(custId, customer);
				if (responseDTO.getStatus().equals("Failure")) {
					return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
				} else {
					return new ResponseEntity<>(responseDTO, HttpStatus.OK);
				}
			} else {
				response.setMessage("Something went wrong.");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
			}
		} catch (Exception e) {
			response.setMessage("Something went wrong");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			logger.error("Error: {} ", e.getMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/search")
	public ResponseEntity<Object> seachEngin(@RequestBody Map<String, String> search) {
		logger.info("Search value: {} ", search);
		logger.info("Search value: {} ", search.get("search"));
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			if (boUserToken.getRolesFromToken().isEmpty()) {
				response.setMessage("Something went wrong.");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
			}
			if (accessHelper.isAccessible("CUSTOMERS_INDIVIDUAL", "VIEW")) {
				response = customerService.getCustomersBySearchValue(search.get("search"));
				if (response.getStatus().equals("Failure")) {
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Something went wrong.");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
			}
		} catch (Exception e) {
			response.setMessage("Something went wrong");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			logger.error("Error: {} ", e.getMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/getDetails/{custId}")
	public ResponseEntity<Object> getCustomerDetails(@PathVariable(name = "custId") String custId) {
		logger.info("Customer id : {} ", custId);
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			if (boUserToken.getRolesFromToken().isEmpty()) {
				response.setMessage("Something went wrong.");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
			}
			if (accessHelper.isAccessible("CUSTOMERS_INDIVIDUAL", "VIEW")) {
				response = customerService.getCustomerDetails(custId);
				if (response.getStatus().equals("Failure")) {
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Something went wrong.");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
			}
		} catch (Exception e) {
			response.setMessage("Something went wrong");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			logger.error("Error: {} ", e.getMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
