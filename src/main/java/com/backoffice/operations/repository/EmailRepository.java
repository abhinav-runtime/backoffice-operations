package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.EmailEntity;

public interface EmailRepository extends JpaRepository<EmailEntity, Long> {
}
