package com.backoffice.operations.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backoffice.operations.entity.CardSettingLog;

@Repository
public interface CardSettingLogRepo extends JpaRepository<CardSettingLog, String> {
	Page<CardSettingLog> findByUniqueKeyOrderByTimeStampDesc(String uniqueKey, Pageable pageable);
}
