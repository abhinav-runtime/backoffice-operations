package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.TransactionLimitsEntity;

public interface TransactionLimitsRepository extends JpaRepository<TransactionLimitsEntity, String> {
	
	TransactionLimitsEntity findByUniqueKey(String uniqueKey);
	
}

