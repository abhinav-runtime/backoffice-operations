package com.backoffice.operations.service;

import com.backoffice.operations.payloads.PromotionDTO;

import java.util.List;

public interface PromotionService {

    List<PromotionDTO> getAllPromotions();

    PromotionDTO getPromotionById(String uniqueKey);

    void markPromotionAsSeen(String uniqueKey);

    PromotionDTO savePromotion(PromotionDTO promotionDTO);

}
