package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.SetCardControlsEntity;

public interface SetCardControlsRepository extends JpaRepository <SetCardControlsEntity, String> {
	
	SetCardControlsEntity findByUniqueKey(String uniqueKey);
	
}
