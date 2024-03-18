package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backoffice.operations.entity.BankList;

@Repository
public interface BankListRepo extends JpaRepository<BankList, String> {
	BankList findByBicCode(String bankCode);
	BankList findByBankName(String bankName);
}
