package com.backoffice.operations.repository;

import com.backoffice.operations.entity.CardTransactionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardTransactionsEntityRepository extends JpaRepository<CardTransactionsEntity, String> {
}
