package com.backoffice.operations.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger logger = LoggerFactory.getLogger(BoProductCategorieServiceImp.class);
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
			logger.error("Error: {}", e.getMessage());
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
			subCategories.setProductTitle(requestDTO.getProductTitle());
			subCategories.setCategories(category);
			subCategories.setExpireDate(requestDTO.getExpireDate());
			subCategories.setIssueDate(requestDTO.getIssueDate());
			subCategories.setBenefits(requestDTO.getBenefits());
			subCategories.setDescription(requestDTO.getDescription());
			subCategories.setFeatures(requestDTO.getFeatures());
			subCategories = boSubCategoriesRepo.save(subCategories);

			response.setStatus("Success");
			response.setMessage("Request Saved Successfully");
			response.setData(subCategories);
		} catch (Exception e) {
			logger.error("Error: {}", e.getMessage());
			response.setStatus("Failed");
			response.setMessage("Something went wrong.");
			response.setData(new HashMap<>());
		}
		return response;
	}

	@Override
	public GenericResponseDTO<Object> getProductSubCategories(String catagoriesId) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		logger.info("catagoriesId: {}", catagoriesId);
		try {
			BoProductCategories categories = boCategoriesRepo.findById(catagoriesId).get();

			Set<Object> responseSubCategories = new LinkedHashSet<>();
			boSubCategoriesRepo.findByCategories(categories).forEach(item -> {
				if (item.getExpireDate() == null) {
					Map<String, Object> subCategoriesTemp = new LinkedHashMap<>();
					subCategoriesTemp.put("id", item.getId());
					subCategoriesTemp.put("subCategoriesName", item.getSubCategoriesName());
					subCategoriesTemp.put("description", item.getDescription());
					subCategoriesTemp.put("features", item.getFeatures());
					subCategoriesTemp.put("benefits", item.getBenefits());
					subCategoriesTemp.put("title", item.getProductTitle());
					responseSubCategories.add(subCategoriesTemp);
				} else {
					Date expireDate = item.getExpireDate();
					if (expireDate.after(new Date())) {
						Map<String, Object> subCategoriesTemp = new LinkedHashMap<>();

						subCategoriesTemp.put("id", item.getId());
						subCategoriesTemp.put("subCategoriesName", item.getSubCategoriesName());
						subCategoriesTemp.put("description", item.getDescription());
						subCategoriesTemp.put("features", item.getFeatures());
						subCategoriesTemp.put("benefits", item.getBenefits());
						subCategoriesTemp.put("title", item.getProductTitle());
						responseSubCategories.add(subCategoriesTemp);
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

		} catch (Exception e) {
			logger.error("Error: {}", e.getMessage());
			response.setStatus("Failed");
			response.setMessage("Something went wrong.");
			response.setData(new HashMap<>());
		}
		return response;
	}

	@Override
	public GenericResponseDTO<Object> getProductCategories() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			List<BoProductCategories> categories = boCategoriesRepo.findAll();
			if (categories != null) {
				response.setStatus("Success");
				response.setMessage("Categories Fetched Successfully");
				response.setData(categories);
			} else {
				response.setStatus("Failed");
				response.setMessage("Something went wrong.");
				response.setData(new HashMap<>());
			}
		} catch (Exception e) {
			logger.error("Error: {}", e.getMessage());
			response.setStatus("Failed");
			response.setMessage("Something went wrong.");
			response.setData(new HashMap<>());
		}

		return response;
	}

	@Override
	public GenericResponseDTO<Object> getProductSubCategoriesForBO(String categoriesId) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		logger.info("categoriesId: {}", categoriesId);
		try {
			BoProductCategories categories = boCategoriesRepo.findById(categoriesId).get();
			logger.info("categories: {}", categories);
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
		} catch (Exception e) {
			logger.error("Error: {}", e.getMessage());
			response.setStatus("Failed");
			response.setMessage("Something went wrong.");
			response.setData(new HashMap<>());
		}

		return response;
	}

	@Override
	public GenericResponseDTO<Object> productRequest(ProductRequestDTO request) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		logger.info("request: {}", request);
		try {
			BoProductSubCategories subCategories = boSubCategoriesRepo.findById(request.getSubCategoriesId()).get();
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
		} catch (Exception e) {
			logger.error("Error: {}", e.getMessage());
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

		try {
			boProductRequestRepo.findAll().forEach(item -> {
				ProductResponseDTO responseDTO = new ProductResponseDTO();
				responseDTO.setName(item.getName());
				responseDTO.setMobileNumber(item.getMobileNumber());
				responseDTO.setEmail(item.getEmail());
				responseDTO.setRequestDate(item.getRequestDate().toString());
				responseDTO.setCategories(item.getCategories());
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
		} catch (Exception e) {
			logger.error("Error: {}", e.getMessage());
			response.setStatus("Failed");
			response.setMessage("Something went wrong.");
			response.setData(new HashMap<>());
		}

		return response;
	}

	@Override
	public GenericResponseDTO<Object> deleteCatagories(String categoriesId) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		logger.info("categoriesId: {}", categoriesId);
		try {
			BoProductCategories categories = boCategoriesRepo.findById(categoriesId).get();
			boSubCategoriesRepo.findByCategories(categories).forEach(item -> {
				boSubCategoriesRepo.delete(item);
			});
			boCategoriesRepo.delete(categories);
			response.setStatus("Success");
			response.setMessage("Request Successfully Processed");
			response.setData(new HashMap<>());
		} catch (Exception e) {
			logger.error("Error: {}", e.getMessage());
			response.setStatus("Failed");
			response.setMessage("Something went wrong.");
			response.setData(new HashMap<>());
		}
		return response;
	}

	@Override
	public GenericResponseDTO<Object> deleteSubCatagories(String categoriesId) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		logger.info("categoriesId: {}", categoriesId);
		try {
			BoProductSubCategories subCategories = boSubCategoriesRepo.findById(categoriesId).get();
			boSubCategoriesRepo.delete(subCategories);
			response.setStatus("Success");
			response.setMessage("Request Successfully Processed");
			response.setData(new HashMap<>());
		} catch (Exception e) {
			logger.error("Error: {}", e.getMessage());
			response.setStatus("Failed");
			response.setMessage("Something went wrong.");
			response.setData(new HashMap<>());
		}
		return response;
	}

	@Override
	public GenericResponseDTO<Object> dateExpiryUpdate(BoProductCategoriesRequestDTO requestDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		logger.info("requestDTO: {}", requestDTO);
		try {
			BoProductSubCategories subCategories = boSubCategoriesRepo
					.findBySubCategoriesName(requestDTO.getSubCategoriesName());
			subCategories.setExpireDate(requestDTO.getExpireDate());
			subCategories = boSubCategoriesRepo.save(subCategories);

			Map<String, String> subCategoriesResponse = new LinkedHashMap<>();
			subCategoriesResponse.put("categories", subCategories.getCategories().getCategoriesName());
			subCategoriesResponse.put("subCategoriesName", subCategories.getSubCategoriesName());
			subCategoriesResponse.put("expireDate", subCategories.getExpireDate().toString());
			response.setStatus("Success");
			response.setMessage("Request Successfully Processed");
			response.setData(subCategoriesResponse);
		} catch (Exception e) {
			logger.error("Error: {}", e.getMessage());
			response.setStatus("Failed");
			response.setMessage("Something went wrong.");
			response.setData(new HashMap<>());
		}
		return response;
	}
}
