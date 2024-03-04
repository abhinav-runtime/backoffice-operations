package com.backoffice.operations.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
	public ResponseEntity<Object> createProductCategories(@RequestBody BoProductCategoriesRequestDTO requestDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		if (accessHelper.isAccessible("PRODUCTS", "PUBLISH") || accessHelper.isAccessible("PRODUCTS", "VIEW")
				|| accessHelper.isAccessible("PRODUCTS", "EDIT")) {
			if (boProductCategorieService.saveProductCategories(requestDTO).getStatus().equals("Failure")) {
				return new ResponseEntity<>(boProductCategorieService.saveProductCategories(requestDTO),
						HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<>(boProductCategorieService.saveProductCategories(requestDTO),
						HttpStatus.CREATED);
			}
		}
		response.setMessage("Something went wrong.");
		response.setStatus("Failure");
		response.setData(new HashMap<>());
		return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@PostMapping("/sub-categories")
	public ResponseEntity<Object> createProductSubCategories(@RequestBody BoProductCategoriesRequestDTO requestDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		if (accessHelper.isAccessible("PRODUCTS", "PUBLISH") || accessHelper.isAccessible("PRODUCTS", "VIEW")
				|| accessHelper.isAccessible("PRODUCTS", "EDIT")) {
			if (boProductCategorieService.saveProductSubCategories(requestDTO).getStatus().equals("Failure")) {
				return new ResponseEntity<>(boProductCategorieService.saveProductSubCategories(requestDTO),
						HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<>(boProductCategorieService.saveProductSubCategories(requestDTO),
						HttpStatus.CREATED);
			}
		}
		response.setMessage("Something went wrong.");
		response.setStatus("Failure");
		response.setData(new HashMap<>());
		return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
	}

	// for mobile application to get all subcategories
	@PreAuthorize(value = "authenticated")
	@GetMapping("/get-sub-categories/{categoriesId}")
	public GenericResponseDTO<Object> getProductSubCategoriesForUser(@PathVariable String categoriesId) {
		return boProductCategorieService.getProductSubCategories(categoriesId);
	}

	@GetMapping("/get-sub-categories-bo/{categoriesId}")
	public ResponseEntity<Object> getSubCategoriesForBO(@PathVariable String categoriesId) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		if (accessHelper.isAccessible("PRODUCTS", "VIEW") || accessHelper.isAccessible("PRODUCTS", "EDIT")) {
			if (boProductCategorieService.getProductSubCategoriesForBO(categoriesId).getStatus().equals("Failure")) {
				return new ResponseEntity<>(boProductCategorieService.getProductSubCategoriesForBO(categoriesId),
						HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<>(boProductCategorieService.getProductSubCategoriesForBO(categoriesId),
						HttpStatus.OK);
			}
		}
		response.setMessage("Something went wrong.");
		response.setStatus("Failure");
		response.setData(new HashMap<>());
		return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
	}

	// for mobile application to send request for product
	@PreAuthorize(value = "authenticated")
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
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		if (accessHelper.isAccessible("PRODUCTS", "VIEW") || accessHelper.isAccessible("PRODUCTS", "EDIT")) {
			if (boProductCategorieService.getRequestDetails().getStatus().equals("Failure")) {
				return new ResponseEntity<>(boProductCategorieService.getRequestDetails(),
						HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<>(boProductCategorieService.getRequestDetails(), HttpStatus.OK);
			}
		}
		response.setMessage("Something went wrong.");
		response.setStatus("Failure");
		response.setData(new HashMap<>());
		return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@DeleteMapping("/delete-sub-categories/{categoriesId}")
	public ResponseEntity<Object> deleteSubCatagories(@PathVariable String categoriesId) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		if (accessHelper.isAccessible("PRODUCTS", "PUBLISH") || accessHelper.isAccessible("PRODUCTS", "VIEW")
				|| accessHelper.isAccessible("PRODUCTS", "EDIT")) {
			if (boProductCategorieService.deleteSubCatagories(categoriesId).getStatus().equals("Failure")) {
				return new ResponseEntity<>(boProductCategorieService.deleteSubCatagories(categoriesId),
						HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<>(boProductCategorieService.deleteSubCatagories(categoriesId), HttpStatus.OK);
			}
		}
		response.setMessage("Something went wrong.");
		response.setStatus("Failure");
		response.setData(new HashMap<>());
		return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@DeleteMapping("/delete-catagories/{categoriesId}")
	public ResponseEntity<Object> deleteCatagories(@PathVariable String categoriesId) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		if (accessHelper.isAccessible("PRODUCTS", "PUBLISH") || accessHelper.isAccessible("PRODUCTS", "VIEW")
				|| accessHelper.isAccessible("PRODUCTS", "EDIT")) {
			if (boProductCategorieService.deleteCatagories(categoriesId).getStatus().equals("Failure")) {
				return new ResponseEntity<>(boProductCategorieService.deleteCatagories(categoriesId),
						HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<>(boProductCategorieService.deleteCatagories(categoriesId), HttpStatus.OK);
			}
		}
		response.setMessage("Something went wrong.");
		response.setStatus("Failure");
		response.setData(new HashMap<>());
		return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@PostMapping("/date-expiry-update")
	public ResponseEntity<Object> expiryDateUpdate(@RequestBody BoProductCategoriesRequestDTO requestDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		if (accessHelper.isAccessible("PRODUCTS", "PUBLISH") || accessHelper.isAccessible("PRODUCTS", "VIEW")
				|| accessHelper.isAccessible("PRODUCTS", "EDIT")) {
			if (boProductCategorieService.dateExpiryUpdate(requestDTO).getStatus().equals("Failure")) {
				return new ResponseEntity<>(boProductCategorieService.dateExpiryUpdate(requestDTO),
						HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<>(boProductCategorieService.dateExpiryUpdate(requestDTO), HttpStatus.OK);
			}
		}
		response.setMessage("Something went wrong.");
		response.setStatus("Failure");
		response.setData(new HashMap<>());
		return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
	}

	// for mobile application to get all categories
	@PreAuthorize(value = "authenticated")
	@GetMapping("/get-categories")
	public GenericResponseDTO<Object> getProductCategories() {
		return boProductCategorieService.getProductCategories();
	}

	@GetMapping("/get-categories-bo")
	public ResponseEntity<Object> getProductsCategoriesForBO() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (boUserToken.getRolesFromToken().isEmpty()) {
			response.setMessage("Something went wrong.");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		if (accessHelper.isAccessible("PRODUCTS", "VIEW") || accessHelper.isAccessible("PRODUCTS", "EDIT")) {
			if (boProductCategorieService.getProductCategories().getStatus().equals("Failure")) {
				return new ResponseEntity<>(boProductCategorieService.getProductCategories(),
						HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<>(boProductCategorieService.getProductCategories(), HttpStatus.OK);
			}
		}
		response.setMessage("Something went wrong.");
		response.setStatus("Failure");
		response.setData(new HashMap<>());
		return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
	}
}
