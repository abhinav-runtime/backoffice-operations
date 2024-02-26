package com.backoffice.operations.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.BoRoleModuleAccessibilityMapping;

public interface BoRoleMuduleAccessMappingRepo extends JpaRepository<BoRoleModuleAccessibilityMapping, String> {
	List<BoRoleModuleAccessibilityMapping> findByModuleIdAndAccessibilityId(String moduleId, String accessibilityId);
	Boolean existsByRoleIdAndModuleIdAndAccessibilityId(String roleId, String moduleId, String accessibilityId);
	List<BoRoleModuleAccessibilityMapping> findByRoleId(String roleId);
}