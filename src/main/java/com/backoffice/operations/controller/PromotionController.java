package com.backoffice.operations.controller;

import com.backoffice.operations.payloads.PromotionDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.PromotionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

	private final PromotionService promotionService;

	public PromotionController(PromotionService promotionService) {
		this.promotionService = promotionService;
	}

	@PostMapping
	public ResponseEntity<Object> createPromotion(@RequestBody PromotionDTO promotionDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		PromotionDTO savedPromotion = promotionService.savePromotion(promotionDTO);
		response.setStatus("Success");
		response.setMessage("Promotion created with ID : " + savedPromotion.getId());
		response.setData(new HashMap<>());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<Object> getAllPromotions() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		List<PromotionDTO> promotions = promotionService.getAllPromotions();
		response.setStatus("Success");
		response.setMessage("Promotions fetched successfully");
		response.setData(promotions);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getPromotionById(@PathVariable String id) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		PromotionDTO promotion = promotionService.getPromotionById(id);
		response.setStatus("Success");
		response.setMessage("Promotion fetched successfully");
		response.setData(promotion);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> markPromotionAsSeen(@PathVariable String id) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		promotionService.markPromotionAsSeen(id);
		response.setStatus("Success");
		response.setMessage("Promotion with ID " + id + " marked as seen.");
		response.setData(new HashMap<>());
		return ResponseEntity.ok(response);
	}
}
