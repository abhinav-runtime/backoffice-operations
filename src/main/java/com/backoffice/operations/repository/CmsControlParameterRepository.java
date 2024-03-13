package com.backoffice.operations.repository;

import com.backoffice.operations.entity.CmsControlParameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CmsControlParameterRepository extends JpaRepository<CmsControlParameter, String> {
    // You can add custom query methods if needed
}