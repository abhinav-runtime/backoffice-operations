package com.backoffice.operations.repository;

import com.backoffice.operations.entity.AccessToken;
import com.backoffice.operations.entity.TransactionPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionPurposeRepository extends JpaRepository<TransactionPurpose, String> {

    TransactionPurpose findByTransactionPurposeCode(String ordinaryTransfer);
}

