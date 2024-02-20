package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.SystemDetail;

public interface SystemDetailRepository extends JpaRepository<SystemDetail, String> {
	SystemDetail findByUniqueKey(String uniqueKeySystemID);
}
