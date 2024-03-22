package com.backoffice.operations.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backoffice.operations.entity.CivilIdEntity;
import java.util.List;

@Repository
public interface CivilIdRepository extends JpaRepository<CivilIdEntity, String> {

	Optional<CivilIdEntity> findByEntityId(String entityId);
	List<CivilIdEntity> findByCivilId(String civilId);
}