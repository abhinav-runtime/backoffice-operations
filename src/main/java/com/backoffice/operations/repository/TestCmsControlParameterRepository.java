package com.backoffice.operations.repository;

import com.backoffice.operations.entity.TestCmsControlParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestCmsControlParameterRepository extends JpaRepository<TestCmsControlParameter, String> {
}