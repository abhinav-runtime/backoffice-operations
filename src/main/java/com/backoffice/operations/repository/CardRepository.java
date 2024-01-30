package com.backoffice.operations.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.CardEntity;

public interface CardRepository extends JpaRepository<CardEntity, Long> {
    List<CardEntity> findByCivilId(String civilId);
}
