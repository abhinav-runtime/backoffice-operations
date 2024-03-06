package com.backoffice.operations.repository;

import com.backoffice.operations.entity.DashboardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardRepository extends JpaRepository<DashboardEntity, String> {

    DashboardEntity findByAccountNumberAndUniqueKey(String accNo, String uniqueKey);
}
