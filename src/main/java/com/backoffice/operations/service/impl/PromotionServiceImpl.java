package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.Promotion;
import com.backoffice.operations.entity.PromotionUser;
import com.backoffice.operations.entity.User;
import com.backoffice.operations.payloads.PromotionDTO;
import com.backoffice.operations.repository.PromotionRepository;
import com.backoffice.operations.repository.PromotionUserRepository;
import com.backoffice.operations.repository.UserRepository;
import com.backoffice.operations.security.JwtTokenProvider;
import com.backoffice.operations.service.PromotionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {

    private static final Logger logger = LoggerFactory.getLogger(PromotionServiceImpl.class);
    private final PromotionRepository promotionRepository;
    private final PromotionUserRepository promotionUserRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;

    public PromotionServiceImpl(PromotionRepository promotionRepository, PromotionUserRepository promotionUserRepository) {
        this.promotionRepository = promotionRepository;
        this.promotionUserRepository = promotionUserRepository;
    }

    @Override
    public PromotionDTO getPromotion(String uniqueKey, String token) {
        try {
            List<PromotionUser> promotionUserList = promotionUserRepository.findAllByUniqueKeyOrderBySeenCountAsc(uniqueKey);
            if (!promotionUserList.isEmpty()) {
                List<Promotion> promotions = promotionRepository.findByIdNotInOrderByPriorityAsc(
                        promotionUserList.stream()
                                .map(promotionUser -> promotionUser.getPromotionId())
                                .collect(Collectors.toList()));
                if (!promotions.isEmpty())
                    return convertToDTOPromotionUser(promotions.get(0));
            } else {
                //First time user
                Promotion promotion = promotionRepository.findPromotionByPriorityOrder();
                return convertToDTO(promotion);
            }
        } catch (Exception e) {
            logger.error("ERROR in class PromotionServiceImpl method getPromotion", e);
        }
        return null;
    }

    private PromotionDTO convertToDTOPromotionUser(Promotion promotion) {
        PromotionDTO dto = new PromotionDTO();
        dto.setId(promotion.getId());
        dto.setImageUrl(promotion.getImageUrl());
        dto.setPriority(promotion.getPriority());
        dto.setText1(promotion.getText1());
        dto.setText2(promotion.getText2());
        dto.setUrlType(promotion.getUrlType());
        dto.setUrl(promotion.getUrl());
        return dto;
    }

    @Override
    public void markPromotionAsSeen(String uniqueKey, String promotionId, String token) {
        String userEmail = jwtTokenProvider.getUsername(token);
        Optional<User> user = userRepository.findByEmail(userEmail);
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new RuntimeException("Promotion not found with id: " + promotionId));
        if (Objects.nonNull(promotion)) {
            PromotionUser promotionUser = promotionUserRepository.findByUniqueKeyAndPromotionId(uniqueKey, promotionId);
            if (Objects.nonNull(promotionUser)) {
                promotionUser.setSeenCount(+1);
            } else {
                promotionUser = new PromotionUser();
                promotionUser.setPromotionId(promotion.getId());
                promotionUser.setSeenAt(LocalDateTime.now());
                promotionUser.setUniqueKey(uniqueKey);
                promotionUser.setSeenCount(1);
            }
            promotionUserRepository.save(promotionUser);
            promotion.setLastUpdatedAt(LocalDateTime.now());
            user.ifPresent(value -> promotion.setUpdatedBy(value.getId()));
            promotionRepository.save(promotion);
        } else {
            logger.error("ERROR in class PromotionServiceImpl method markPromotionAsSeen");
        }
    }

    @Override
    public PromotionDTO savePromotion(PromotionDTO promotionDTO, String token) {
        String userEmail = jwtTokenProvider.getUsername(token);
        Optional<User> user = userRepository.findByEmail(userEmail);

        Promotion promotion = new Promotion();
        promotion.setImageUrl(promotionDTO.getImageUrl());
        promotion.setPriority(promotionDTO.getPriority());
        promotion.setText1(promotionDTO.getText1());
        promotion.setText2(promotionDTO.getText2());
        promotion.setCreatedAt(LocalDateTime.now());
        promotion.setUrl(promotionDTO.getUrl());
        promotion.setUrlType(promotionDTO.getUrlType());
        user.ifPresent(value -> promotion.setCreatedBy(value.getId()));
        Promotion savedPromotion = promotionRepository.save(promotion);
        return convertToDTO(savedPromotion);
    }

    private PromotionDTO convertToDTO(Promotion promotion) {
        PromotionDTO dto = new PromotionDTO();
        dto.setId(promotion.getId());
        dto.setImageUrl(promotion.getImageUrl());
        dto.setPriority(promotion.getPriority());
        dto.setText1(promotion.getText1());
        dto.setText2(promotion.getText2());
        dto.setUrlType(promotion.getUrlType());
        dto.setUrl(promotion.getUrl());
        return dto;
    }
}
