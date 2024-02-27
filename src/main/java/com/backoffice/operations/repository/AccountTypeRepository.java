package com.backoffice.operations.repository;

import com.backoffice.operations.entity.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, String> {

    AccountType findByCbsProductCode(String productCode);
}

