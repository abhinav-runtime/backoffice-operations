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
import com.backoffice.operations.service.BOCustomerService;

@RestController
@RequestMapping("/bo/v1/customers")
public class BOCustomerController {

	@Autowired
	private BOCustomerService customerService;

	@GetMapping("/all")
	public ResponseEntity<Object> getAllCustomers() {

		return new ResponseEntity<>(customerService.getAllCustomers(), HttpStatus.OK);

	}

	@PostMapping("/edit/{custId}")
	public ResponseEntity<Object> editCustomer(@PathVariable String custId, @RequestBody CustomerRequestDTO custoemr) {
		if (customerService.editCustomer(custId, custoemr) == null) {
			return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(customerService.editCustomer(custId, custoemr), HttpStatus.OK);
		}
	}
}
