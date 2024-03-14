package com.backoffice.operations.controller;

import com.backoffice.operations.exceptions.DuplicateEntryException;
import com.backoffice.operations.payloads.CmsControlParameterDTO;
import com.backoffice.operations.payloads.TransferParameterDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.security.BOUserToken;
import com.backoffice.operations.service.CmsControlParameterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/bo/v1/cms-control-parameters")
public class CmsControlParameterController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CmsControlParameterController.class);
	private final CmsControlParameterService service;

	@Autowired
	public CmsControlParameterController(CmsControlParameterService service) {
		this.service = service;
	}

	@Autowired
	private BOUserToken boUserToken;

	@GetMapping
	public ResponseEntity<Object> getAllParameters() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			if (boUserToken.getRolesFromToken().isEmpty()) {
				response.setMessage("Something went wrong.");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
			} else {
				List<CmsControlParameterDTO> parameters = service.getAllParameters();
				if (parameters.isEmpty()) {
					response.setStatus("Failure");
					response.setMessage("No data present");
					response.setData(new HashMap<>());
					return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
				} else {
					response.setStatus("Success");
					response.setMessage("Success");
					response.setData(parameters);
					return new ResponseEntity<>(response, HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occurred while fetching all cms control Parameters", e);
			response.setStatus("Failure");
			response.setMessage("No data present");
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getParameterById(@PathVariable String id) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			if (boUserToken.getRolesFromToken().isEmpty()) {
				response.setMessage("Something went wrong.");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
			} else {
				CmsControlParameterDTO parameter = service.getParameterById(id);

				if (parameter == null) {
					response.setStatus("Failure");
					response.setMessage("Data not found with id: " + id);
					response.setData(new HashMap<>());
					return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
				} else {
					response.setStatus("Success");
					response.setMessage("Success");
					response.setData(parameter);
					return new ResponseEntity<>(response, HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occurred while fetching cms control Parameters by ID: {}", id, e);
			response.setStatus("Failure");
			response.setMessage("Data not found with id: " + id);
			response.setData(new HashMap<>());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping
	public ResponseEntity<Object> createParameter(@RequestBody CmsControlParameterDTO parameterDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			if (boUserToken.getRolesFromToken().isEmpty()) {
				response.setMessage("Something went wrong.");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
			} else {
				CmsControlParameterDTO createdParameter = service.createParameter(parameterDTO);
				response.setStatus("Success");
				response.setMessage("Success");
				response.setData(createdParameter);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (DuplicateEntryException e) {
			LOGGER.error("Duplicate entry error occurred while creating cms control Parameters", e);
			response.setStatus("Failure");
			response.setMessage("Duplicate entry");
			response.setData(new HashMap<>());
		} catch (Exception e) {
			LOGGER.error("Error occurred while creating cms control Parameters", e);
			response.setStatus("Failure");
			response.setMessage("Something went wrong");
			response.setData(new HashMap<>());
		}
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> updateParameter(@PathVariable String id,
			@RequestBody CmsControlParameterDTO parameterDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			if (boUserToken.getRolesFromToken().isEmpty()) {
				response.setMessage("Something went wrong.");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
			} else {
				CmsControlParameterDTO updatedParameter = service.updateParameter(id, parameterDTO);
				if (updatedParameter == null) {
					response.setStatus("Failure");
					response.setMessage("No data present");
					response.setData(null);
					return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
				} else {
					response.setStatus("Success");
					response.setMessage("data updated successfully");
					response.setData(updatedParameter);
					return new ResponseEntity<>(response, HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occurred while updating cms control Parameters by ID: {}", id, e);
			response.setStatus("Failure");
			response.setMessage("Something went wrong");
			response.setData(null);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteParameter(@PathVariable String id) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			if (boUserToken.getRolesFromToken().isEmpty()) {
				response.setMessage("Something went wrong.");
				response.setStatus("Failure");
				response.setData(new HashMap<>());
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
			}
			boolean deletionSuccessful = service.deleteParameter(id);
			if (deletionSuccessful) {
				response.setMessage("Entry deleted successfully");
				response.setStatus("Success");
				response.setData(null);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Entry not found for deletion");
				response.setStatus("Failure");
				response.setData(null);
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOGGER.error("Error occurred while deleting TransferParameter by ID: {}", id, e);
			response.setMessage("Delete failed");
			response.setStatus("Failure");
			response.setData(null);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}