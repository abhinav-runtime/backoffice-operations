package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backoffice.operations.entity.BOModuleName;

@Repository
public interface BoModuleNameRepo extends JpaRepository<BOModuleName, String> {
	BOModuleName findByModuleName(String moduleName);
	Boolean existsByModuleName(String moduleName);
}
