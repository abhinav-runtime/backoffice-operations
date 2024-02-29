package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.Promotion;
import com.backoffice.operations.payloads.PromotionDTO;
import com.backoffice.operations.repository.PromotionRepository;
import com.backoffice.operations.service.PromotionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionServiceImpl(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public List<PromotionDTO> getAllPromotions() {
        List<Promotion> promotions = promotionRepository.findAll();
        return promotions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public PromotionDTO getPromotionById(String id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found with ID: " + id));
        return convertToDTO(promotion);
    }

    @Override
    public void markPromotionAsSeen(String id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found with ID: " + id));
        promotion.setSeenByUser(true);
        promotionRepository.save(promotion);
    }

    @Override
    public PromotionDTO savePromotion(PromotionDTO promotionDTO) {
        Promotion promotion = new Promotion();
        promotion.setImageUrl(promotionDTO.getImageUrl());
        promotion.setUniqueKey(promotionDTO.getUniqueKey());
        promotion.setSeenByUser(Boolean.FALSE);
        Promotion savedPromotion = promotionRepository.save(promotion);
        return convertToDTO(savedPromotion);
    }

    private PromotionDTO convertToDTO(Promotion promotion) {
        PromotionDTO dto = new PromotionDTO();
        dto.setId(promotion.getId());
        dto.setImageUrl(promotion.getImageUrl());
        dto.setSeenByUser(promotion.isSeenByUser());
        dto.setUniqueKey(promotion.getUniqueKey());
        return dto;
    }
}
