package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backoffice.operations.entity.CardEntity;

public interface CardRepository extends JpaRepository<CardEntity, String> {
//    List<CardEntity> findByCivilId(String civilId);
	CardEntity findByUniqueKeyCivilId(String uniqueKeyCivilId);
}
