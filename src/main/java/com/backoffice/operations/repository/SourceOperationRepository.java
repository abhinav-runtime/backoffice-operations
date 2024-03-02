package com.backoffice.operations.repository;

import com.backoffice.operations.entity.SourceOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceOperationRepository extends JpaRepository<SourceOperation, String> {
    SourceOperation findBySourceCode(String selfTransfer);
}

