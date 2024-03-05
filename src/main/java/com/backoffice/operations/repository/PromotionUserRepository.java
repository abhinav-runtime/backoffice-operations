package com.backoffice.operations.repository;

import com.backoffice.operations.entity.PromotionUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionUserRepository extends JpaRepository<PromotionUser, String> {

    List<PromotionUser> findAllByUniqueKeyOrderBySeenCountAsc(String uniqueKey);

    PromotionUser findByUniqueKeyAndPromotionId(String uniqueKey, String promotionId);
}
