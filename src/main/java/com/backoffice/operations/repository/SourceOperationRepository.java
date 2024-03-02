package com.backoffice.operations.repository;

import com.backoffice.operations.entity.AccessToken;
import com.backoffice.operations.entity.SourceOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SourceOperationRepository extends JpaRepository<SourceOperation, String> {

}
