package com.backoffice.operations.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.BOAccessibility;
import com.backoffice.operations.entity.BOModuleName;
import com.backoffice.operations.entity.BORole;
import com.backoffice.operations.entity.BoRoleModuleAccessibilityMapping;
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
	@Autowired
	private BORolesRepo boRolesRepo;
	@Autowired
	private BoAccessibilityRepo boAccessibilityRepo;
	@Autowired
	private BoModuleNameRepo boModuleNameRepo;
	@Autowired
	private BoRoleMuduleAccessMappingRepo boMappingRepo;

	@Override
	public GenericResponseDTO<Object> roleAccessAssign(BORoleDTO requestRoleDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		if (!boRolesRepo.existsByName(requestRoleDTO.getName())) {
			response.setMessage("Need to create role first");
			response.setStatus("Failure");
			response.setData(null);
		} else {
			String roleId = boRolesRepo.findByName(requestRoleDTO.getName()).getId();
			requestRoleDTO.getModules().forEach(module -> {
				String moduleId = boModuleNameRepo.findByModuleName(module.getModuleName()).getId();
				module.getAccessTypes().forEach(accessType -> {
					String accessTypeId = boAccessibilityRepo.findByAccessType(accessType).getId();
					BoRoleModuleAccessibilityMapping mapping = new BoRoleModuleAccessibilityMapping();
					mapping.setRoleId(roleId);
					mapping.setModuleId(moduleId);
					mapping.setAccessibilityId(accessTypeId);
					if (!boMappingRepo.existsByRoleIdAndModuleIdAndAccessibilityId(roleId, moduleId, accessTypeId)) {
						boMappingRepo.save(mapping);
					}
				});
			});

			response.setMessage("Role Created Successfully");
			response.setStatus("Success");
			response.setData(boMappingRepo.findByRoleId(roleId));
		}
		return response;
	}

	@Override
	public GenericResponseDTO<Object> createAccessibility(BOModuleOrAccessTypeRequest requestDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<Object>();
		if (!boAccessibilityRepo.existsByAccessType(requestDTO.getName())) {
			BOAccessibility accessibility = new BOAccessibility();
			accessibility.setAccessType(requestDTO.getName());
			accessibility = boAccessibilityRepo.save(accessibility);
			response.setMessage("Accessibility Created Successfully");
			response.setStatus("Success");
			response.setData(accessibility);
		}
		else {
			response.setMessage("Accessibility already exists");
			response.setStatus("Failure");
			response.setData(null);
		}
		return response;
	}
	
	@Override
	public GenericResponseDTO<Object> createRole(BoRoleRequestDTO requestDTO){
		GenericResponseDTO<Object> response = new GenericResponseDTO<Object>();
		if (!boRolesRepo.existsByName(requestDTO.getName())) {
			BORole role = new BORole();
			role.setName(requestDTO.getName());
			role.setPriority(requestDTO.getPriority());		
			role = boRolesRepo.save(role);
			
			response.setMessage("Role Created Successfully");
			response.setStatus("Success");
			response.setData(role);			
		} else {
			response.setMessage("Role already exists");
			response.setStatus("Failure");
			response.setData(null);
		}
		return response;
	}

	@Override
	public GenericResponseDTO<Object> createModule(BOModuleOrAccessTypeRequest requestDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<Object>();
		if (!boModuleNameRepo.existsByModuleName(requestDTO.getName())) {
			BOModuleName module = new BOModuleName();
			module.setModuleName(requestDTO.getName());
			module = boModuleNameRepo.save(module);
			response.setMessage("Module Created Successfully");
			response.setStatus("Success");
			response.setData(module);
		}
		else {
			response.setMessage("Module already exists");
			response.setStatus("Failure");
			response.setData(null);
		}
		return response;
	}
	
	
}
