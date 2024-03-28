package com.backoffice.operations.controller;

import com.backoffice.operations.exceptions.DuplicateEntryException;
import com.backoffice.operations.payloads.TransferParameterDTO;
import com.backoffice.operations.payloads.TransfersParameterDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.TransferParameterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/transfers-parameter")
public class TransferParameterController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransferParameterController.class);
	@Autowired
	private TransferParameterService TransferParameterService;

	@GetMapping("/getbyid/{id}")
	public GenericResponseDTO<Object> getTransferParameterById(@PathVariable Long id) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			TransfersParameterDTO transferParameterDTO = TransferParameterService.findById(id);
			if (transferParameterDTO == null) {
				response.setStatus("Failure");
				response.setMessage("Data not found with id: " + id);
			} else {
				response.setStatus("Success");
				response.setMessage("Data found successfully");
				response.setData(transferParameterDTO);
			}
		} catch (Exception e) {
			response.setStatus("Failure");
			response.setMessage("Error occurred while fetching TransferParameter by ID: " + id);
			response.setData(null);
		}
		return response;
	}

	// GetMapping for getting all transfer parameters
	@GetMapping("/getAll")
	public GenericResponseDTO<Object> getAllTransferParameters() {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			List<TransfersParameterDTO> transferParameterDTOs = TransferParameterService.findAll();
			response.setStatus("Success");
			response.setMessage("Data retrieved successfully");
			response.setData(transferParameterDTOs);
		} catch (Exception e) {
			LOGGER.error("Error in getAllTransferParameters {}",e);
			response.setStatus("Failure");
			response.setMessage("Error occurred while fetching TransferParameters");
			response.setData(null);
		}
		return response;
	}

	// PostMapping for creating a transfer parameter
	@PostMapping("/create")
	public GenericResponseDTO<Object> createTransferParameter(@RequestBody TransfersParameterDTO transferParameterDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			TransfersParameterDTO createdDTO = TransferParameterService.create(transferParameterDTO);
			response.setStatus("Success");
			response.setMessage("TransferParameter created successfully");
			response.setData(createdDTO);
		} catch (Exception e) {
			LOGGER.error("Error in createTransferParameter {}",e);
			response.setStatus("Failure");
			response.setMessage("Error occurred while creating TransferParameter");
			response.setData(null);
		}
		return response;
	}

	// PutMapping for updating a transfer parameter
	@PutMapping("/update/{id}")
	public GenericResponseDTO<Object> updateTransferParameter(@PathVariable Long id,
															  @RequestBody TransfersParameterDTO transferParameterDTO
															  ) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			TransfersParameterDTO updatedDTO = TransferParameterService.update(id, transferParameterDTO);
			response.setStatus("Success");
			response.setMessage("TransferParameter updated successfully");
			response.setData(updatedDTO);
		} catch (Exception e) {
			LOGGER.error("Error in updateTransferParameter {}",e);
			response.setStatus("Failure");
			response.setMessage("Error occurred while updating TransferParameter with ID: " + id);
			response.setData(null);
		}
		return response;
	}

	// DeleteMapping for deleting a transfer parameter
	@DeleteMapping("/delete/{id}")
	public GenericResponseDTO<Object> deleteTransferParameter(@PathVariable Long id
															  ) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			TransferParameterService.delete(id);
			response.setStatus("Success");
			response.setMessage("TransferParameter deleted successfully");
			response.setData(null);
		} catch (Exception e) {
			LOGGER.error("Error in deleteTransferParameter {}",e);
			response.setStatus("Failure");
			response.setMessage("Error occurred while deleting TransferParameter with ID: " + id);
			response.setData(null);
		}
		return response;
	}
}