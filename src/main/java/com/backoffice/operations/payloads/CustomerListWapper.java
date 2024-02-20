package com.backoffice.operations.payloads;

import java.util.List;

import com.backoffice.operations.entity.Customer;


public class CustomerListWapper {
	
	private List<Customer> customers;

    public CustomerListWapper(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
    
}