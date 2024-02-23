package com.backoffice.operations.service;

import java.util.List;

import com.backoffice.operations.entity.Customer;
import com.backoffice.operations.payloads.CustomerRequestDTO;
import com.backoffice.operations.payloads.CustomerResponseDTO;
//import com.backoffice.operations.payloads.ValidationResultDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;

public interface BOCustomerService {
	
	public GenericResponseDTO<List<Customer>> getAllCustomers();
	public GenericResponseDTO<CustomerResponseDTO> editCustomer(String custId, CustomerRequestDTO customerRequestDTO);
	public void addOnboardCustomer(String uniqueToken);
}
