package com.backoffice.operations.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backoffice.operations.entity.BoSystemLogEntity;

@Repository
public interface BoLogginglogRepository extends JpaRepository<BoSystemLogEntity, String> {
	List<BoSystemLogEntity> findAllByOrderByTimestampDesc();
	Page<BoSystemLogEntity> findByOrderByTimestampDesc(Pageable pageable);
}
