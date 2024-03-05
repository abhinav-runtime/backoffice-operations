package com.backoffice.operations.repository;

import com.backoffice.operations.entity.Promotion;
import com.backoffice.operations.entity.PromotionUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, String> {
    @Query(value = "SELECT * FROM az_promotions_bk ORDER BY priority ASC LIMIT 1", nativeQuery = true)
    Promotion findPromotionByPriorityOrder();

    List<Promotion> findByIdNotInOrderByPriorityAsc(List<String> ids);

}
