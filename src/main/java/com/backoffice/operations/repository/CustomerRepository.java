package com.backoffice.operations.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String> {
  Customer findByCustId(String custId);
}
