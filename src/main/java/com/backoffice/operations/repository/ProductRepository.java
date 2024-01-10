package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.Product;

public interface ProductRepository extends JpaRepository<Product, String>{

}
