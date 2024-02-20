package com.backoffice.operations.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.Customer;
import com.backoffice.operations.payloads.CustomerRequestDTO;
import com.backoffice.operations.payloads.CustomerResponseDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.CustomerRepository;
import com.backoffice.operations.service.BOCustomerService;

@Service
public class BOCustomerServiceImp implements BOCustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public GenericResponseDTO<List<Customer>> getAllCustomers() {
		
		List<Customer> allCustomers = customerRepository.findAll();
		GenericResponseDTO<List<Customer>> customerResponse = new GenericResponseDTO<>();
	    if (allCustomers.size() != 0) {
	    	customerResponse.setStatus("success");
	    	customerResponse.setMessage("All customers List");
	    	customerResponse.setData(allCustomers);			
		}
	    else {
			customerResponse.setStatus("Failure");
			customerResponse.setMessage("No customers found");
			customerResponse.setData(null);
		}
	    
	    
	    return customerResponse;
	}

	@SuppressWarnings("null")
	@Override
	public GenericResponseDTO<CustomerResponseDTO> editCustomer(String custId, CustomerRequestDTO customerRequestDTO) {
		Customer customer = customerRepository.findByCustId(custId);
		GenericResponseDTO<CustomerResponseDTO> response = new GenericResponseDTO<>();
		if (customer != null) {
			if  (customerRequestDTO.getName() != null) {
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
		}else {
			CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();
			customerResponseDTO.setCustId(custId);
			customerResponseDTO.setName(null);
			customerResponseDTO.setBranch(null);
			customerResponseDTO.setCountry(null);
			customerResponseDTO.setDateRegistered(null);
			
			response.setStatus("Failure");
			response.setMessage("Customer not found");
			response.setData(customerResponseDTO);
		}
		return response;
	}

}
