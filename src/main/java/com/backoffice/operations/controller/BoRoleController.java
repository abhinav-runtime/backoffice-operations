package com.backoffice.operations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.payloads.BOModuleOrAccessTypeRequest;
import com.backoffice.operations.payloads.BORoleDTO;
import com.backoffice.operations.payloads.BoRoleRequestDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.security.BOUserToken;
import com.backoffice.operations.service.BORoleService;
import com.backoffice.operations.service.impl.BoAccessHelper;

@RestController
@RequestMapping("/bo/v1/role")
public class BoRoleController {

	@Autowired
	private BORoleService boRoleService;
	@Autowired
	private BOUserToken boUserToken;
	@Autowired
	private BoAccessHelper accessHelper;

	@PostMapping("/accessibilityAssign")
	public GenericResponseDTO<Object> roleAccessibilityAssign(@RequestBody BORoleDTO requestRoleDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			if (boUserToken.getRolesFromToken().isEmpty()) {
				response.setMessage("Token not found or expired");
				response.setStatus("Failure");
				response.setData(null);
				return response;
			}
			if (accessHelper.isAccessible("AUTHORIZATION", "PUBLISH")||accessHelper.isAccessible("AUTHORIZATION", "EDIT")) {
				return boRoleService.roleAccessAssign(requestRoleDTO);
			}
			response.setMessage("User not have permission to operate this action");
			response.setStatus("Failure");
			response.setData(null);
		} catch (Exception e) {
			response.setMessage("Something went wrong");
			response.setStatus("Failure");
			response.setData(e.getMessage());
		}
		return response;
	}
	
	@PostMapping("/roleCreate")
	public GenericResponseDTO<Object> roleCreate(@RequestBody BoRoleRequestDTO requestDTO){
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			
			if (boUserToken.getRolesFromToken().isEmpty()) {
				response.setMessage("Token not found or expired");
				response.setStatus("Failure");
				response.setData(null);
				return response;
			}
			if (accessHelper.isAccessible("AUTHORIZATION", "PUBLISH")||accessHelper.isAccessible("AUTHORIZATION", "EDIT")) {
				return boRoleService.createRole(requestDTO);
			}
			response.setMessage("User not have permission to operate this action");
			response.setStatus("Failure");
			response.setData(null);
		} catch (Exception e) {
			response.setMessage("Something went wrong");
			response.setStatus("Failure");
			response.setData(null);
		}
		return response;
	}
	
	@PostMapping("/moduleCreate")
	public GenericResponseDTO<Object> moduleCreate(@RequestBody BOModuleOrAccessTypeRequest requestDTO){
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			
			if (boUserToken.getRolesFromToken().isEmpty()) {
				response.setMessage("Token not found or expired");
				response.setStatus("Failure");
				response.setData(null);
				return response;
			}
			if (accessHelper.isAccessible("AUTHORIZATION", "PUBLISH")||accessHelper.isAccessible("AUTHORIZATION", "EDIT")) {
				return boRoleService.createModule(requestDTO);
			}
			response.setMessage("User not have permission to operate this action");
			response.setStatus("Failure");
			response.setData(null);
		} catch (Exception e) {
			response.setMessage("Something went wrong");
			response.setStatus("Failure");
			response.setData(e.getMessage());
		}
		return response;
	}
	
	@PostMapping("/accessibilityCreate")
	public GenericResponseDTO<Object> accessibilityCreate(@RequestBody BOModuleOrAccessTypeRequest requestDTO){
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {

			if (boUserToken.getRolesFromToken().isEmpty()) {
				response.setMessage("Token not found or expired");
				response.setStatus("Failure");
				response.setData(null);
				return response;
			}
			if (accessHelper.isAccessible("AUTHORIZATION", "PUBLISH")||accessHelper.isAccessible("AUTHORIZATION", "EDIT")) {
				return boRoleService.createAccessibility(requestDTO);
			}
			response.setMessage("User not have permission to operate this action");
			response.setStatus("Failure");
			response.setData(null);
		} catch (Exception e) {
			response.setMessage("Something went wrong");
			response.setStatus("Failure");
			response.setData(null);
		}
		return response;
	}
	
	
}
