package com.backoffice.operations.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.SystemDetail;

import java.util.List;
import java.util.Optional;

public interface SystemDetailRepository extends JpaRepository<SystemDetail, String> {
    Optional<SystemDetail> findByCivilId(String civilId);
    List<SystemDetail> findAllByCivilId(String civilId);
    List<SystemDetail> findAllByCivilIdOrderByCreatedDesc(String civilId);
    Page<SystemDetail> findAllByCivilIdOrderByCreatedDesc(String civilId, Pageable pageable);
}
