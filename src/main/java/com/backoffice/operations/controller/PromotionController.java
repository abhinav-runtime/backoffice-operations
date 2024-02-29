package com.backoffice.operations.controller;

import com.backoffice.operations.payloads.PromotionDTO;
import com.backoffice.operations.service.PromotionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @PostMapping
    public ResponseEntity<String> createPromotion(@RequestBody PromotionDTO promotionDTO) {
        PromotionDTO savedPromotion = promotionService.savePromotion(promotionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Promotion created with ID: " + savedPromotion.getId());
    }

    @GetMapping
    public ResponseEntity<List<PromotionDTO>> getAllPromotions() {
        List<PromotionDTO> promotions = promotionService.getAllPromotions();
        return ResponseEntity.ok(promotions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionDTO> getPromotionById(@PathVariable String id) {
        PromotionDTO promotion = promotionService.getPromotionById(id);
        return ResponseEntity.ok(promotion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> markPromotionAsSeen(@PathVariable String id) {
        promotionService.markPromotionAsSeen(id);
        return ResponseEntity.ok("Promotion with ID " + id + " marked as seen.");
    }
}
