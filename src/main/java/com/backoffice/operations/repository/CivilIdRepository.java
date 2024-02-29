package com.backoffice.operations.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backoffice.operations.entity.CivilIdEntity;

public interface CivilIdRepository extends JpaRepository<CivilIdEntity, String> {

	Optional<CivilIdEntity> findByEntityId(String entityId);

}