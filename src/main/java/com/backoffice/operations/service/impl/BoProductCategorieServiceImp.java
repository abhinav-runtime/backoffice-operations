package com.backoffice.operations.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.BoProductCategories;
import com.backoffice.operations.entity.BoProductRequest;
import com.backoffice.operations.entity.BoProductSubCategories;
import com.backoffice.operations.payloads.BoProductCategoriesRequestDTO;
import com.backoffice.operations.payloads.ProductRequestDTO;
import com.backoffice.operations.payloads.ProductResponseDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.BoProductCategoriesRepo;
import com.backoffice.operations.repository.BoProductRequestRepo;
import com.backoffice.operations.repository.BoProductSubCategoriesRepo;
import com.backoffice.operations.service.BoProductCategorieService;

@Service
public class BoProductCategorieServiceImp implements BoProductCategorieService {
	@Autowired
	private BoProductCategoriesRepo boCategoriesRepo;
	@Autowired
	private BoProductSubCategoriesRepo boSubCategoriesRepo;
	@Autowired
	private BoProductRequestRepo boProductRequestRepo;

	@Override
	public GenericResponseDTO<Object> saveProductCategories(BoProductCategoriesRequestDTO requestDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			BoProductCategories categories = new BoProductCategories();
			categories.setCategoriesName(requestDTO.getCategoriesName());
			categories = boCategoriesRepo.save(categories);
			response.setStatus("Success");
			response.setMessage("Request Saved Successfully");
			response.setData(categories);
		} catch (Exception e) {
			response.setStatus("Failed");
			response.setMessage("Something went wrong.");
			response.setData(new HashMap<>());
		}
		return response;
	}

	@Override
	public GenericResponseDTO<Object> saveProductSubCategories(BoProductCategoriesRequestDTO requestDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			BoProductCategories category = boCategoriesRepo.findByCategoriesName(requestDTO.getCategoriesName());
			if (category == null) {
				// Handle case when the category does not exist
				response.setStatus("Failed");
				response.setMessage("Something went wrong.");
				response.setData(new HashMap<>());
				return response;
			}

			BoProductSubCategories subCategories = new BoProductSubCategories();
			subCategories.setSubCategoriesName(requestDTO.getSubCategoriesName());
			subCategories.setCategories(category);
			subCategories.setExpireDate(requestDTO.getExpireDate());
			subCategories = boSubCategoriesRepo.save(subCategories);

			response.setStatus("Success");
			response.setMessage("Request Saved Successfully");
			response.setData(subCategories);
		} catch (Exception e) {
			response.setStatus("Failed");
			response.setMessage("Something went wrong.");
			response.setData(new HashMap<>());
		}
		return response;
	}

	@Override
	public GenericResponseDTO<Object> getProductSubCategories(String categoriesName) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		BoProductCategories categories = boCategoriesRepo.findByCategoriesName(categoriesName);

		List<String> responseSubCategories = new ArrayList<>();
		boSubCategoriesRepo.findByCategories(categories).forEach(item -> {
			if (item.getExpireDate() == null) {
				responseSubCategories.add(item.getSubCategoriesName());
			} else {
				Date expireDate = item.getExpireDate();
				if (expireDate.after(new Date())) {
					responseSubCategories.add(item.getSubCategoriesName());
				}
			}
		});
		if (categories != null) {
			response.setStatus("Success");
			response.setMessage("Sub Categories Fetched Successfully");
			response.setData(responseSubCategories);
		} else {
			response.setStatus("Failed");
			response.setMessage("Something went wrong.");
			response.setData(new HashMap<>());
		}
		return response;
	}

	@Override
	public GenericResponseDTO<Object> getProductSubCategoriesForBO(String categoriesName) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		BoProductCategories categories = boCategoriesRepo.findByCategoriesName(categoriesName);

		List<BoProductSubCategories> responseSubCategories = boSubCategoriesRepo.findByCategories(categories);
		if (categories != null) {
			response.setStatus("Success");
			response.setMessage("Sub Categories Fetched Successfully");
			response.setData(responseSubCategories);
		} else {
			response.setStatus("Failed");
			response.setMessage("Something went wrong.");
			response.setData(new HashMap<>());
		}
		return response;
	}

	@Override
	public GenericResponseDTO<Object> productRequest(ProductRequestDTO request) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		BoProductSubCategories subCategories = boSubCategoriesRepo
				.findBySubCategoriesName(request.getSubCategoriesName());
		if (subCategories != null) {
			BoProductRequest requestProduct = new BoProductRequest();
			requestProduct.setName(request.getName());
			requestProduct.setMobileNumber(request.getMobileNumber());
			requestProduct.setEmail(request.getEmail());
			requestProduct.setSubCategories(subCategories.getSubCategoriesName());
			requestProduct.setCategories(subCategories.getCategories().getCategoriesName());
			requestProduct = boProductRequestRepo.save(requestProduct);

			response.setStatus("Success");
			response.setMessage("Request Saved Successfully");
			response.setData(new HashMap<>());
		} else {
			response.setStatus("Failed");
			response.setMessage("Something went wrong.");
			response.setData(new HashMap<>());
		}
		return response;
	}

	@Override
	public GenericResponseDTO<Object> getRequestDetails() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		List<ProductResponseDTO> responseData = new ArrayList<>();

		boProductRequestRepo.findAll().forEach(item -> {
			ProductResponseDTO responseDTO = new ProductResponseDTO();
			responseDTO.setName(item.getName());
			responseDTO.setMobileNumber(item.getMobileNumber());
			responseDTO.setEmail(item.getEmail());
			responseDTO.setRequestDate(item.getRequestDate().toString());
			responseDTO.setCategories(item.getSubCategories());
			responseDTO.setSubCategories(item.getSubCategories());
			responseData.add(responseDTO);
		});

		if (responseData.size() == 0) {
			response.setStatus("Failed");
			response.setMessage("Something went wrong.");
			response.setData(new HashMap<>());
		} else {
			response.setStatus("Success");
			response.setMessage("Request Fetched Successfully");
			response.setData(responseData);
		}
		return response;
	}

	@Override
	public GenericResponseDTO<Object> deleteCatagories(String categoriesName) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			BoProductCategories categories = boCategoriesRepo.findByCategoriesName(categoriesName);
			boSubCategoriesRepo.findByCategories(categories).forEach(item -> {
				boSubCategoriesRepo.delete(item);
			});
			boCategoriesRepo.delete(categories);
			response.setStatus("Success");
			response.setMessage("Request Successfully Processed");
			response.setData(new HashMap<>());
		} catch (Exception e) {
			response.setStatus("Failed");
			response.setMessage("Something went wrong.");
			response.setData(new HashMap<>());
		}
		return response;
	}

	@Override
	public GenericResponseDTO<Object> deleteSubCatagories(String subCategoriesName) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			BoProductSubCategories subCategories = boSubCategoriesRepo.findBySubCategoriesName(subCategoriesName);
			boSubCategoriesRepo.delete(subCategories);
			response.setStatus("Success");
			response.setMessage("Request Successfully Processed");
			response.setData(new HashMap<>());
		} catch (Exception e) {
			response.setStatus("Failed");
			response.setMessage("Something went wrong.");
			response.setData(new HashMap<>());
		}
		return response;
	}

	@Override
	public GenericResponseDTO<Object> dateExpiryUpdate(BoProductCategoriesRequestDTO requestDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			BoProductSubCategories subCategories = boSubCategoriesRepo
					.findBySubCategoriesName(requestDTO.getSubCategoriesName());
			subCategories.setExpireDate(requestDTO.getExpireDate());
			boSubCategoriesRepo.save(subCategories);
			response.setStatus("Success");
			response.setMessage("Request Successfully Processed");
			response.setData(new HashMap<>());
		} catch (Exception e) {
			response.setStatus("Failed");
			response.setMessage("Something went wrong.");
			response.setData(new HashMap<>());
		}
		return response;
	}
}
