package com.backoffice.operations.controller;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	private static final Logger logger = LoggerFactory.getLogger(BoRoleController.class);
	@Autowired
	private BORoleService boRoleService;
	@Autowired
	private BOUserToken boUserToken;
	@Autowired
	private BoAccessHelper accessHelper;

	@PostMapping("/accessibilityAssign")
	public ResponseEntity<Object> roleAccessibilityAssign(@RequestBody BORoleDTO requestRoleDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			if (boUserToken.getRolesFromToken().isEmpty()) {
				response.setMessage("Token not found or expired");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
			}
			if (accessHelper.isAccessible("AUTHORIZATION", "PUBLISH")
					|| accessHelper.isAccessible("AUTHORIZATION", "EDIT")) {
				response = boRoleService.roleAccessAssign(requestRoleDTO);
				if (response.getStatus().equals("Failure")) {
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				} else {
					return new ResponseEntity<>(response, HttpStatus.OK);
				}
			}
			response.setMessage("User not have permission to operate this action");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
		} catch (Exception e) {
			response.setMessage("Something went wrong");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			logger.error("Error {} : ", e.getMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/roleCreate")
	public ResponseEntity<Object> roleCreate(@RequestBody BoRoleRequestDTO requestDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			if (boUserToken.getRolesFromToken().isEmpty()) {
				response.setMessage("Token not found or expired");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
			}
			if (accessHelper.isAccessible("AUTHORIZATION", "PUBLISH")
					|| accessHelper.isAccessible("AUTHORIZATION", "EDIT")) {
				response = boRoleService.createRole(requestDTO);
				if (response.getStatus().equals("Failure")) {
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				} else {
					return new ResponseEntity<>(response, HttpStatus.CREATED);
				}
			}
			response.setMessage("User not have permission to operate this action");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
		} catch (Exception e) {
			response.setMessage("Something went wrong");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			logger.error("Error {} : ", e.getMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/moduleCreate")
	public ResponseEntity<Object> moduleCreate(@RequestBody BOModuleOrAccessTypeRequest requestDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			logger.info("Request : {}", requestDTO);
			if (boUserToken.getRolesFromToken().isEmpty()) {
				response.setMessage("Token not found or expired");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
			}
			if (accessHelper.isAccessible("AUTHORIZATION", "PUBLISH")
					|| accessHelper.isAccessible("AUTHORIZATION", "EDIT")) {
				response = boRoleService.createModule(requestDTO);
				if (response.getStatus().equals("Failure")) {
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				} else {
					return new ResponseEntity<>(response, HttpStatus.CREATED);
				}
			}
			response.setMessage("User not have permission to operate this action");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
		} catch (Exception e) {
			logger.error("Error {} : ", e.getMessage());
			response.setMessage("Something went wrong");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
		}
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/accessibilityCreate")
	public ResponseEntity<Object> accessibilityCreate(@RequestBody BOModuleOrAccessTypeRequest requestDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			logger.info("Request : {}", requestDTO);
			if (boUserToken.getRolesFromToken().isEmpty()) {
				response.setMessage("Token not found or expired");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
			}
			if (accessHelper.isAccessible("AUTHORIZATION", "PUBLISH")
					|| accessHelper.isAccessible("AUTHORIZATION", "EDIT")) {
				response = boRoleService.createAccessibility(requestDTO);
				if (response.getStatus().equals("Failure")) {
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				} else {
					return new ResponseEntity<>(response, HttpStatus.CREATED);
				}
			}
			response.setMessage("User not have permission to operate this action");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
		} catch (Exception e) {
			logger.error("Error {} : ", e.getMessage());
			response.setMessage("Something went wrong");
			response.setStatus("Failure");
			response.setData(new HashMap<>());
		}
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
