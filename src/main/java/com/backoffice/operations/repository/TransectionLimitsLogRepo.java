package com.backoffice.operations.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backoffice.operations.entity.TransectionLimitsLog;

@Repository
public interface TransectionLimitsLogRepo extends JpaRepository<TransectionLimitsLog, String> {
	Page<TransectionLimitsLog> findByUniqueKeyOrderByTimeStampDesc(String uniqueKey, Pageable pageable);
}
