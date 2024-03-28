package com.backoffice.operations.repository;

import com.backoffice.operations.entity.TransferParameter;
import com.backoffice.operations.entity.TransfersParameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferParameterRepository extends JpaRepository<TransfersParameter, Long> {

}
