package com.backoffice.operations.service.impl;

import java.util.HashMap;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.BOAccessibility;
import com.backoffice.operations.entity.BOModuleName;
import com.backoffice.operations.entity.BORole;
import com.backoffice.operations.entity.BoRoleModuleAccessibilityMapping;
import com.backoffice.operations.payloads.BOModuleDTO;
import com.backoffice.operations.payloads.BOModuleOrAccessTypeRequest;
import com.backoffice.operations.payloads.BORoleDTO;
import com.backoffice.operations.payloads.BoRoleRequestDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.BORolesRepo;
import com.backoffice.operations.repository.BoAccessibilityRepo;
import com.backoffice.operations.repository.BoModuleNameRepo;
import com.backoffice.operations.repository.BoRoleMuduleAccessMappingRepo;
import com.backoffice.operations.service.BORoleService;

@Service
public class BORoleServiceImp implements BORoleService {
	private static final Logger logger = LoggerFactory.getLogger(BORoleServiceImp.class);
	@Autowired
	private BORolesRepo boRolesRepo;
	@Autowired
	private BoAccessibilityRepo boAccessibilityRepo;
	@Autowired
	private BoModuleNameRepo boModuleNameRepo;
	@Autowired
	private BoRoleMuduleAccessMappingRepo boMappingRepo;
	@Autowired
	private BoAccessHelper boAccessHelper;

	@Override
	public GenericResponseDTO<Object> roleAccessAssign(BORoleDTO requestRoleDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			if (!boRolesRepo.existsByName(requestRoleDTO.getName())) {
				response.setMessage("Need to create role first");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
			} else {
				String roleId = boRolesRepo.findByName(requestRoleDTO.getName()).getId();

				Set<BOModuleDTO> allModules = requestRoleDTO.getModules();
				for (BOModuleDTO module : allModules) {
					if (!boModuleNameRepo.existsByModuleName(module.getModuleName())) {
						response.setMessage("Need to create " + module.getModuleName() + " module first");
						response.setStatus("Failure");
						response.setData(new HashMap<>());
						return response;
					}
					String moduleId = boModuleNameRepo.findByModuleName(module.getModuleName()).getId();
					Set<String> allAccessSet = module.getAccessTypes();
					for (String accessType : allAccessSet) {
						if (!boAccessibilityRepo.existsByAccessType(accessType)) {
							response.setMessage("Need to create " + accessType + " accessibility first");
							response.setStatus("Failure");
							response.setData(new HashMap<>());
							return response;
						}
						String accessTypeId = boAccessibilityRepo.findByAccessType(accessType).getId();
						BoRoleModuleAccessibilityMapping mapping = new BoRoleModuleAccessibilityMapping();
						mapping.setRoleId(roleId);
						mapping.setModuleId(moduleId);
						mapping.setAccessibilityId(accessTypeId);
						if (!boMappingRepo.existsByRoleIdAndModuleIdAndAccessibilityId(roleId, moduleId,
								accessTypeId)) {
							boMappingRepo.save(mapping);
						}
					}
				}
				response.setMessage("Access assign successfully");
				response.setStatus("Success");
				response.setData(boAccessHelper.accessAssignResponse(boMappingRepo.findByRoleId(roleId)));
			}
		} catch (Exception e) {
			logger.error("Error {} : ", e.getMessage());
			response.setMessage("Something went wrong");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
		}
		return response;
	}

	@Override
	public GenericResponseDTO<Object> createAccessibility(BOModuleOrAccessTypeRequest requestDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			if (!boAccessibilityRepo.existsByAccessType(requestDTO.getName())) {
				BOAccessibility accessibility = new BOAccessibility();
				accessibility.setAccessType(requestDTO.getName());
				accessibility = boAccessibilityRepo.save(accessibility);
				response.setMessage("Accessibility Created Successfully");
				response.setStatus("Success");
				response.setData(accessibility);
			} else {
				response.setMessage("Something went wrong.");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
			}
		} catch (Exception e) {
			logger.error("Error {} : ", e.getMessage());
			response.setMessage("Something went wrong");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
		}
		return response;
	}

	@Override
	public GenericResponseDTO<Object> createRole(BoRoleRequestDTO requestDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			if (!boRolesRepo.existsByName(requestDTO.getName())) {
				BORole role = new BORole();
				role.setName(requestDTO.getName());
				role.setPriority(requestDTO.getPriority());
				role = boRolesRepo.save(role);

				response.setMessage("Role Created Successfully");
				response.setStatus("Success");
				response.setData(role);
			} else {
				response.setMessage("Something went wrong.");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
			}
		} catch (Exception e) {
			logger.error("Error {} : ", e.getMessage());
			response.setMessage("Something went wrong");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
		}
		return response;
	}

	@Override
	public GenericResponseDTO<Object> createModule(BOModuleOrAccessTypeRequest requestDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<Object>();
		try {
			if (!boModuleNameRepo.existsByModuleName(requestDTO.getName())) {
				BOModuleName module = new BOModuleName();
				module.setModuleName(requestDTO.getName());
				module = boModuleNameRepo.save(module);
				response.setMessage("Module Created Successfully");
				response.setStatus("Success");
				response.setData(module);
			} else {
				response.setMessage("Something went wrong.");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
			}
		} catch (Exception e) {
			logger.error("Error {} : ", e.getMessage());
			response.setMessage("Something went wrong");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
		}
		return response;
	}

}
