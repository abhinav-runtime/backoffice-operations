package com.backoffice.operations.repository;

import com.backoffice.operations.entity.AccountTransactionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTransactionsEntityRepository extends JpaRepository<AccountTransactionsEntity, String> {
}
