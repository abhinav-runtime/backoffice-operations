package com.backoffice.operations.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.BOUser;
import java.util.List;


public interface BOUsersRepo extends JpaRepository<BOUser, String> {
	Optional<BOUser> findByEmail(String email);
	BOUser findByEmailAndBranch(String email, String branch);
	Boolean existsByEmailAndBranch(String email, String branch);
	Boolean existsByEmail(String email);
	Boolean existsByUsername(String username);
}
