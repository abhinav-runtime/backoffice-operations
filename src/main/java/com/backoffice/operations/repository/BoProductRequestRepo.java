package com.backoffice.operations.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.BoProductRequest;
import com.backoffice.operations.entity.BoProductSubCategories;

public interface BoProductRequestRepo extends JpaRepository<BoProductRequest, String> {
	List<BoProductRequest> findBySubCategories(BoProductSubCategories subCategories);
}
