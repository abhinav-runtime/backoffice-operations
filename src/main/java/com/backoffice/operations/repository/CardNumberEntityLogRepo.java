package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backoffice.operations.entity.CardNumberEntityLog;

@Repository
public interface CardNumberEntityLogRepo extends JpaRepository<CardNumberEntityLog, String> {
	CardNumberEntityLog findByUniqueKey(String uniqueKey);
	Boolean existsByUniqueKey(String uniqueKey);
}
