package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.AccessToken;

public interface AccessTokenRepository extends JpaRepository<AccessToken, String> {

}
