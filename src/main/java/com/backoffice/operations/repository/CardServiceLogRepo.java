package com.backoffice.operations.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backoffice.operations.entity.CardServiceLog;

@Repository
public interface CardServiceLogRepo extends JpaRepository<CardServiceLog, String> {
	Page<CardServiceLog> findByEntityIdOrderByTimeStampDesc(String entityId, Pageable pageable);
}
