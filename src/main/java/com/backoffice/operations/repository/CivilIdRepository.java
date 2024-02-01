package com.backoffice.operations.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backoffice.operations.entity.CivilIdEntity;

public interface CivilIdRepository extends JpaRepository<CivilIdEntity, String> {
    @Query("SELECT e.entityId FROM CivilIdEntity e WHERE e.civilId = :civilId")
    Optional<String> findEntityIdByCivilId(String civilId);
}