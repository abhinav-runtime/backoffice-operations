package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.TransactionCode;
import com.backoffice.operations.enums.TransferType;

public interface TransactionCodeRepo extends JpaRepository<TransactionCode, String> {
	TransactionCode findByTransferType(TransferType transferType);
}
