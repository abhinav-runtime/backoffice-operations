package com.backoffice.operations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.payloads.BoProductCategoriesRequestDTO;
import com.backoffice.operations.payloads.ProductRequestDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.BoProductCategorieService;

@RestController
@RequestMapping("/bo/v1/product")
public class BoProductController {
	@Autowired
	private BoProductCategorieService boProductCategorieService;

	@PostMapping("/categories")
	public GenericResponseDTO<Object> createProductCategories(@RequestBody BoProductCategoriesRequestDTO requestDTO) {
		return boProductCategorieService.saveProductCategories(requestDTO);
	}

	@PostMapping("/sub-categories")
	public GenericResponseDTO<Object> createProductSubCategories(
			@RequestBody BoProductCategoriesRequestDTO requestDTO) {
		return boProductCategorieService.saveProductSubCategories(requestDTO);
	}
	
	@GetMapping("/get-sub-categories/{categories}")
	public GenericResponseDTO<Object> getProductSubCategories(@PathVariable String categories) {
		return boProductCategorieService.getProductSubCategories(categories);
	}
	
	@PostMapping("/request")
	public ResponseEntity<Object> createProductRequest(@RequestBody ProductRequestDTO requestDTO) {	
		return new ResponseEntity<>(boProductCategorieService.productRequest(requestDTO), HttpStatus.OK);
	}
	@GetMapping("/get-request-product")
	public ResponseEntity<Object> getRequestProduct() {
		return new ResponseEntity<>(boProductCategorieService.getRequestDetails(), HttpStatus.OK);
	}
	@DeleteMapping("/delete-sub-categories/{subCategories}")
	public ResponseEntity<Object> deleteSubCatagories(@PathVariable String subCategories) {
		return new ResponseEntity<>(boProductCategorieService.deleteSubCatagories(subCategories), HttpStatus.OK);
	}
	@DeleteMapping("/delete-catagories/{categories}")
	public ResponseEntity<Object> deleteCatagories(@PathVariable String categories) {
		return new ResponseEntity<>(boProductCategorieService.deleteCatagories(categories), HttpStatus.OK);
	}
}
