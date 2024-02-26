package com.backoffice.operations.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.BORole;

public interface BORolesRepo extends JpaRepository<BORole, String> {
	BORole findByName(String name);
	Boolean existsByName(String name);
}
