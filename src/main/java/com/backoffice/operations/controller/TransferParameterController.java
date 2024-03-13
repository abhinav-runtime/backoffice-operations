package com.backoffice.operations.controller;
import com.backoffice.operations.exceptions.DuplicateEntryException;
import com.backoffice.operations.payloads.TransferParameterDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.TransferParameterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/transfer-parameters")
public class TransferParameterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferParameterController.class);

    @Autowired
    private TransferParameterService transferParameterService;

    @GetMapping("/{id}")
    public GenericResponseDTO<Object> getTransferParameterById(@PathVariable String id) {
        GenericResponseDTO<Object> response = new GenericResponseDTO<>();
        try {
            TransferParameterDTO transferParameterDTO = transferParameterService.findById(id);
            response.setStatus("Success");
            if(transferParameterDTO == null){
                response.setMessage("Data not found with id: " + id);
            }else{
                response.setMessage("Success");
            }
            response.setData(transferParameterDTO);
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching TransferParameter by ID: {}", id, e);
            response.setStatus("Failure");
            response.setMessage("Data not found with id: " + id);
            response.setData(new HashMap<>());
        }
        return response;
    }

    @GetMapping
    public GenericResponseDTO<Object> getAllTransferParameters() {
        GenericResponseDTO<Object> response = new GenericResponseDTO<>();
        try {
            List<TransferParameterDTO> transferParameterDTOs = transferParameterService.findAll();
            response.setStatus("Success");
            if(transferParameterDTOs.isEmpty()){
                response.setMessage("No data present");
            }else{
                response.setMessage("Success");
            }
            response.setData(transferParameterDTOs);
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching all TransferParameters", e);
            response.setStatus("Failure");
            response.setMessage("No data present");
            response.setData(new HashMap<>());
        }
        return response;
    }

    @PostMapping
    public GenericResponseDTO<Object> createTransferParameter(
            @RequestBody TransferParameterDTO transferParameterDTO) {
        GenericResponseDTO<Object> response = new GenericResponseDTO<>();
        try {
            TransferParameterDTO createdTransferParameter = transferParameterService.save(transferParameterDTO);
            response.setStatus("Success");
            response.setMessage("Success");
            response.setData(createdTransferParameter);
        } catch (DuplicateEntryException e) {
            LOGGER.error("Duplicate entry error occurred while creating TransferParameter", e);
            response.setStatus("Failure");
            response.setMessage("Duplicate entry");
            response.setData(null);
        } catch (Exception e) {
            LOGGER.error("Error occurred while creating TransferParameter", e);
            response.setStatus("Failure");
            response.setMessage("Something went wrong");
            response.setData(null);
        }
        return response;
    }
    @PutMapping("/{id}")
    public GenericResponseDTO<Object> updateTransferParameter(
            @PathVariable String id, @RequestBody TransferParameterDTO updatedTransferParameterDTO) {
        GenericResponseDTO<Object> response = new GenericResponseDTO<>();
        try {
            TransferParameterDTO updatedEntity = transferParameterService.update(id, updatedTransferParameterDTO);
            response.setStatus("Success");
            if(updatedEntity == null){
                response.setMessage("No data present");
                response.setData(null);
            }
            else {
                response.setMessage("data updated successfully");
                response.setData(updatedEntity);
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while updating TransferParameter by ID: {}", id, e);
            response.setStatus("Failure");
            response.setMessage("Something went wrong");
            response.setData(null);
        }
        return response;
    }

    @DeleteMapping("/{id}")
    public GenericResponseDTO<Object> deleteTransferParameter(@PathVariable String id) {
        GenericResponseDTO<Object> response = new GenericResponseDTO<>();
        try {
            boolean deletionSuccessful = transferParameterService.delete(id);
            if (deletionSuccessful) {
                response.setMessage("Entry deleted successfully");
                response.setStatus("Success");
                response.setData(null);
            } else {
                response.setMessage("Entry not found for deletion");
                response.setStatus("Failure");
                response.setData(null);
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while deleting TransferParameter by ID: {}", id, e);
            response.setMessage("Delete failed");
            response.setStatus("Failure");
            response.setData(null);
        }
        return response;
    }
}