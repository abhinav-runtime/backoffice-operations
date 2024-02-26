package com.backoffice.operations.service;

import com.backoffice.operations.payloads.BOModuleOrAccessTypeRequest;
import com.backoffice.operations.payloads.BORoleDTO;
import com.backoffice.operations.payloads.BoRoleRequestDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;

public interface BORoleService {
	GenericResponseDTO<Object> roleAccessAssign(BORoleDTO roles);
	GenericResponseDTO<Object> createAccessibility(BOModuleOrAccessTypeRequest requestDTO);
	GenericResponseDTO<Object> createModule(BOModuleOrAccessTypeRequest requestDTO);
	GenericResponseDTO<Object> createRole(BoRoleRequestDTO requestDTO);
}
