package com.backoffice.operations.service;

import com.backoffice.operations.payloads.BoProductCategoriesRequestDTO;
import com.backoffice.operations.payloads.ProductRequestDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;

public interface BoProductCategorieService {
	
	GenericResponseDTO<Object> saveProductCategories(BoProductCategoriesRequestDTO categories);
	GenericResponseDTO<Object> saveProductSubCategories(BoProductCategoriesRequestDTO categories);
	GenericResponseDTO<Object> getProductSubCategories(String categoriesName);
	GenericResponseDTO<Object> productRequest(ProductRequestDTO request);
	GenericResponseDTO<Object> getRequestDetails();
	GenericResponseDTO<Object> deleteSubCatagories(String subCategoriesName);
	GenericResponseDTO<Object> deleteCatagories(String categoriesName);
	GenericResponseDTO<Object> getProductSubCategoriesForBO(String categoriesName);
	GenericResponseDTO<Object> dateExpiryUpdate(BoProductCategoriesRequestDTO requestDTO);
}
