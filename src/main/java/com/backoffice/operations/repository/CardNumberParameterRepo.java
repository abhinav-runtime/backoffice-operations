package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backoffice.operations.entity.CardNumberParameter;

@Repository
public interface CardNumberParameterRepo extends JpaRepository<CardNumberParameter, Long> {

}
