package com.backoffice.operations.service;

import com.backoffice.operations.payloads.BoCategoriesAndSubCategoriesUpdateDTO;
import com.backoffice.operations.payloads.BoProductCategoriesRequestDTO;
import com.backoffice.operations.payloads.ProductRequestDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;

public interface BoProductCategorieService {
	
	GenericResponseDTO<Object> saveProductCategories(BoProductCategoriesRequestDTO categories);
	GenericResponseDTO<Object> saveProductSubCategories(BoProductCategoriesRequestDTO categories);
	GenericResponseDTO<Object> getProductSubCategories(String categoriesId);
	GenericResponseDTO<Object> productRequest(ProductRequestDTO request);
	GenericResponseDTO<Object> getRequestDetails();
	GenericResponseDTO<Object> deleteSubCatagories(String subCategoriesId);
	GenericResponseDTO<Object> deleteCatagories(String categoriesId);
	GenericResponseDTO<Object> getProductSubCategoriesForBO(String categoriesId);
	GenericResponseDTO<Object> getProductCategories();
	GenericResponseDTO<Object> categoriesUpdate(BoCategoriesAndSubCategoriesUpdateDTO requestDTO);
	GenericResponseDTO<Object> subCategoriesUpdate(BoCategoriesAndSubCategoriesUpdateDTO requestDTO);
}
