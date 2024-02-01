package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.PinRequestEntity;

public interface PinRequestRepository extends JpaRepository<PinRequestEntity, Long> {

}
