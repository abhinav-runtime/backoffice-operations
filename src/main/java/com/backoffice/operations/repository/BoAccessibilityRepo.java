package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.BOAccessibility;

public interface BoAccessibilityRepo extends JpaRepository<BOAccessibility, String> {
	BOAccessibility findByAccessType(String accessType);
	Boolean existsByAccessType(String accessType);
}
