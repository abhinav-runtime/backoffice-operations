package com.backoffice.operations.service;

import com.backoffice.operations.payloads.PromotionDTO;

public interface PromotionService {

    void markPromotionAsSeen(String uniqueKey, String promotionId, String token);

    PromotionDTO savePromotion(PromotionDTO promotionDTO, String token);

    PromotionDTO getPromotion(String uniqueKey, String token);
}
