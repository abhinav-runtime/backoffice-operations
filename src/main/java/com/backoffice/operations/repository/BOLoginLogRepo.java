package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backoffice.operations.entity.BOLoginLog;

@Repository
public interface BOLoginLogRepo extends JpaRepository<BOLoginLog, String> {

}
