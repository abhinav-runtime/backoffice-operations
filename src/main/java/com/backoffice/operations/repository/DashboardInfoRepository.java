package com.backoffice.operations.repository;

import com.backoffice.operations.entity.DashboardInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardInfoRepository extends JpaRepository<DashboardInfoEntity, String> {
}
