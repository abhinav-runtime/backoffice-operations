package com.backoffice.operations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.payloads.CustomerRequestDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.security.BOUserToken;
//import com.backoffice.operations.security.BOUserToken;
import com.backoffice.operations.service.BOCustomerService;
import com.backoffice.operations.service.impl.BoAccessHelper;

@RestController
@RequestMapping("/bo/v1/customers")
public class BOCustomerController {

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
				response.setMessage("Token not found or expired");
				response.setStatus("Failure");
				response.setData(null);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			if (accessHelper.isAccessible("CUSTOMERS_INDIVIDUAL", "VIEW")||accessHelper.isAccessible("CUSTOMERS_INDIVIDUAL", "EDIT")) {
				return new ResponseEntity<>(customerService.getAllCustomers(), HttpStatus.OK);
			}else {
				
			}
			response.setMessage("User not have permission to operate this action");
			response.setStatus("Failure");
			response.setData(null);
		} catch (Exception e) {
			response.setMessage("Something went wrong");
			response.setStatus("Failure");
			response.setData(e.getMessage());
		}
	
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/edit/{custId}")
	public ResponseEntity<Object> editCustomer(@PathVariable String custId, @RequestBody CustomerRequestDTO custoemr) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {

			if (boUserToken.getRolesFromToken().isEmpty()) {
				response.setMessage("Token not found or expired");
				response.setStatus("Failure");
				response.setData(null);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			if (accessHelper.isAccessible("CUSTOMERS_INDIVIDUAL", "EDIT")) {
				return new ResponseEntity<>(customerService.editCustomer(custId, custoemr), HttpStatus.OK);
			}else {
				
			}
			response.setMessage("User not have permission to operate this action");
			response.setStatus("Failure");
			response.setData(null);
		} catch (Exception e) {
			response.setMessage("Something went wrong");
			response.setStatus("Failure");
			response.setData(e.getMessage());
		}
	
		return new ResponseEntity<>(response, HttpStatus.OK);
		
		
	}
}
