package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.OtpEntity;

public interface OtpRepository extends JpaRepository<OtpEntity, Long> {

	OtpEntity findByUserId(String userId);

}
