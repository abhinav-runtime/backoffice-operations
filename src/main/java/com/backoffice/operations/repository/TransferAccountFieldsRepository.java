package com.backoffice.operations.repository;

import com.backoffice.operations.entity.TransferAccountFields;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferAccountFieldsRepository extends JpaRepository<TransferAccountFields, String> {

    TransferAccountFields findByTransferType(String transferType);

}
