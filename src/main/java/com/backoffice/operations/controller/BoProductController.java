package com.backoffice.operations.controller;

import java.util.HashMap;

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
import com.backoffice.operations.security.BOUserToken;
import com.backoffice.operations.service.BoProductCategorieService;
import com.backoffice.operations.service.impl.BoAccessHelper;

@RestController
@RequestMapping("/bo/v1/product")
public class BoProductController {
	@Autowired
	private BoProductCategorieService boProductCategorieService;
	@Autowired
	private BOUserToken boUserToken;
	@Autowired
	private BoAccessHelper accessHelper;

	@PostMapping("/categories")
	public GenericResponseDTO<Object> createProductCategories(@RequestBody BoProductCategoriesRequestDTO requestDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(null);
			return response;
		}
		if (accessHelper.isAccessible("PRODUCTS", "PUBLISH") || accessHelper.isAccessible("PRODUCTS", "VIEW")
				|| accessHelper.isAccessible("PRODUCTS", "EDIT")) {
			return boProductCategorieService.saveProductCategories(requestDTO);
		}
		response.setMessage("Something went wrong.");
		response.setStatus("Failure");
		response.setData(new HashMap<>());
		return response;
	}

	@PostMapping("/sub-categories")
	public GenericResponseDTO<Object> createProductSubCategories(
			@RequestBody BoProductCategoriesRequestDTO requestDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(null);
			return response;
		}
		if (accessHelper.isAccessible("PRODUCTS", "PUBLISH") || accessHelper.isAccessible("PRODUCTS", "VIEW")
				|| accessHelper.isAccessible("PRODUCTS", "EDIT")) {
			return boProductCategorieService.saveProductSubCategories(requestDTO);
		}
		response.setMessage("Something went wrong.");
		response.setStatus("Failure");
		response.setData(new HashMap<>());
		return response;
	}

	@GetMapping("/get-sub-categories/{categories}")
	public GenericResponseDTO<Object> getProductSubCategoriesForUser(@PathVariable String categories) {
		return boProductCategorieService.getProductSubCategories(categories);
	}

	@GetMapping("/get-sub-categories-bo/{categories}")
	public GenericResponseDTO<Object> getSubCategoriesForBO(@PathVariable String categories) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(null);
			return response;
		}
		if (accessHelper.isAccessible("PRODUCTS", "VIEW") || accessHelper.isAccessible("PRODUCTS", "EDIT")) {
			return boProductCategorieService.getProductSubCategoriesForBO(categories);
		}
		response.setMessage("Something went wrong.");
		response.setStatus("Failure");
		response.setData(new HashMap<>());
		return response;
	}

	@PostMapping("/request")
	public ResponseEntity<Object> createProductRequest(@RequestBody ProductRequestDTO requestDTO) {
		return new ResponseEntity<>(boProductCategorieService.productRequest(requestDTO), HttpStatus.OK);
	}

	@GetMapping("/get-request-product")
	public ResponseEntity<Object> getRequestProduct() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(null);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		if (accessHelper.isAccessible("PRODUCTS", "VIEW") || accessHelper.isAccessible("PRODUCTS", "EDIT")) {
			return new ResponseEntity<>(boProductCategorieService.getRequestDetails(), HttpStatus.OK);
		}
		response.setMessage("Something went wrong.");
		response.setStatus("Failure");
		response.setData(new HashMap<>());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/delete-sub-categories/{subCategories}")
	public ResponseEntity<Object> deleteSubCatagories(@PathVariable String subCategories) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(null);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		if (accessHelper.isAccessible("PRODUCTS", "PUBLISH") || accessHelper.isAccessible("PRODUCTS", "VIEW")
				|| accessHelper.isAccessible("PRODUCTS", "EDIT")) {
			return new ResponseEntity<>(boProductCategorieService.deleteSubCatagories(subCategories), HttpStatus.OK);
		}
		response.setMessage("Something went wrong.");
		response.setStatus("Failure");
		response.setData(new HashMap<>());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/delete-catagories/{categories}")
	public ResponseEntity<Object> deleteCatagories(@PathVariable String categories) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(null);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		if (accessHelper.isAccessible("PRODUCTS", "PUBLISH") || accessHelper.isAccessible("PRODUCTS", "VIEW")
				|| accessHelper.isAccessible("PRODUCTS", "EDIT")) {
			return new ResponseEntity<>(boProductCategorieService.deleteCatagories(categories), HttpStatus.OK);
		}
		response.setMessage("Something went wrong.");
		response.setStatus("Failure");
		response.setData(new HashMap<>());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	@PostMapping("/date-expiry-update")
	public GenericResponseDTO<Object> expiryDateUpdate(@RequestBody BoProductCategoriesRequestDTO requestDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(null);
			return response;
		}
		if (accessHelper.isAccessible("PRODUCTS", "PUBLISH") || accessHelper.isAccessible("PRODUCTS", "VIEW")
				|| accessHelper.isAccessible("PRODUCTS", "EDIT")) {
			return boProductCategorieService.dateExpiryUpdate(requestDTO);
		}
		response.setMessage("Something went wrong.");
		response.setStatus("Failure");
		response.setData(new HashMap<>());
		return response;
	}
}
