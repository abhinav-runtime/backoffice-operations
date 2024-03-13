package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backoffice.operations.entity.DevicesParameter;

@Repository
public interface DevicesParameterRepo extends JpaRepository<DevicesParameter, Long> {

}
