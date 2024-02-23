package com.backoffice.operations.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.BOUser;

public interface BOUsersRepo extends JpaRepository<BOUser, String> {
	Optional<BOUser> findByEmail(String email);
	Boolean existsByEmail(String email);
	Boolean existsByUsername(String username);
}
