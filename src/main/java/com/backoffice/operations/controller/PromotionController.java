package com.backoffice.operations.controller;

import com.backoffice.operations.payloads.PromotionDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.PromotionService;
import org.springframework.http.HttpHeaders;
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
	public ResponseEntity<Object> createPromotion(@RequestBody PromotionDTO promotionDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		PromotionDTO savedPromotion = promotionService.savePromotion(promotionDTO, token.substring("Bearer ".length()));
		response.setStatus("Success");
		response.setMessage("Promotion created with ID : " + savedPromotion.getId());
		response.setData(new HashMap<>());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<Object> getPromotion(@RequestParam(required = false) String uniqueKey , @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		PromotionDTO promotion = promotionService.getPromotion(uniqueKey, token.substring("Bearer ".length()));
		response.setStatus("Success");
		response.setMessage("Promotion fetched successfully");
		response.setData(promotion);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/markAsSeen")
	public ResponseEntity<Object> markPromotionAsSeen(@RequestParam(required = false) String uniqueKey, @RequestParam(required = false) String promotionId,
													  @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		promotionService.markPromotionAsSeen(uniqueKey, promotionId, token.substring("Bearer ".length()));
		response.setStatus("Success");
		response.setMessage("Promotion with ID " + promotionId + " marked as seen.");
		response.setData(new HashMap<>());
		return ResponseEntity.ok(response);
	}
}
