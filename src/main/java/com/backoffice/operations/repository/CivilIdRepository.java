package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backoffice.operations.entity.CivilIdEntity;

public interface CivilIdRepository extends JpaRepository<CivilIdEntity, String> {

}