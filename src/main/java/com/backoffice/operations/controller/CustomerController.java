package com.backoffice.operations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.backoffice.operations.entity.Customer;
import com.backoffice.operations.payloads.CustomerRequestDTO;
import com.backoffice.operations.payloads.CustomerResponseDTO;
import com.backoffice.operations.repository.CustomerRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
	
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public List<CustomerResponseDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{custId}")
    public CustomerResponseDTO getCustomerById(@PathVariable String custId) {
        Customer customer = customerRepository.findById(custId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + custId));
        return mapToResponseDTO(customer);
    }

    @PostMapping
    public CustomerResponseDTO createCustomer(@RequestBody CustomerRequestDTO customerRequestDTO) {
        Customer customer = new Customer();
        // Map fields from request DTO to entity
        customer.setName(customerRequestDTO.getName());
        customer.setBranch(customerRequestDTO.getBranch());
        customer.setCountry(customerRequestDTO.getCountry());
        // Set other fields...

        Customer savedCustomer = customerRepository.save(customer);

        return mapToResponseDTO(savedCustomer);
    }

    @PutMapping("/{custId}")
    public CustomerResponseDTO updateCustomer(
            @PathVariable String custId, @RequestBody CustomerRequestDTO updatedCustomerDTO) {
        Customer existingCustomer = customerRepository.findById(custId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + custId));

        // Map fields from request DTO to entity
        existingCustomer.setName(updatedCustomerDTO.getName());
        existingCustomer.setBranch(updatedCustomerDTO.getBranch());
        existingCustomer.setCountry(updatedCustomerDTO.getCountry());
        // Set other fields...

        Customer updatedCustomer = customerRepository.save(existingCustomer);

        return mapToResponseDTO(updatedCustomer);
    }

    @DeleteMapping("/{custId}")
    public void deleteCustomer(@PathVariable String custId) {
        customerRepository.deleteById(custId);
    }
    
 // Helper method to map entity to response DTO
    private CustomerResponseDTO mapToResponseDTO(Customer customer) {
        CustomerResponseDTO responseDTO = new CustomerResponseDTO();
        // Map fields from entity to response DTO
        responseDTO.setCustId(customer.getCustId());
        responseDTO.setName(customer.getName());
        responseDTO.setBranch(customer.getBranch());
        responseDTO.setCountry(customer.getCountry());
        responseDTO.setDateRegistered(customer.getDateRegistered());
        return responseDTO;
    }

}
