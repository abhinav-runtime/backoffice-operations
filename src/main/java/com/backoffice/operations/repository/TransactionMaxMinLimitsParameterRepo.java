package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.TransactionMaxMinLimitsParameter;

public interface TransactionMaxMinLimitsParameterRepo extends JpaRepository <TransactionMaxMinLimitsParameter,Long>{
	
}
