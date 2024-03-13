package com.backoffice.operations.controller;


import com.backoffice.operations.exceptions.DuplicateEntryException;
import com.backoffice.operations.payloads.CmsControlParameterDTO;
import com.backoffice.operations.payloads.TransferParameterDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
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
@RequestMapping("/api/cms-control-parameters")
public class CmsControlParameterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsControlParameterController.class);
    private final CmsControlParameterService service;

    @Autowired
    public CmsControlParameterController(CmsControlParameterService service) {
        this.service = service;
    }

    @GetMapping
    public GenericResponseDTO<Object> getAllParameters() {
        GenericResponseDTO<Object> response = new GenericResponseDTO<>();
        try {
            List<CmsControlParameterDTO> parameters = service.getAllParameters();
            response.setStatus("Success");
            if(parameters.isEmpty()){
                response.setMessage("No data present");
            }else{
                response.setMessage("Success");
            }
            response.setData(parameters);
        }catch (Exception e){
            LOGGER.error("Error occurred while fetching all cms control Parameters", e);
            response.setStatus("Failure");
            response.setMessage("No data present");
            response.setData(new HashMap<>());
        }
        return response;
    }

    @GetMapping("/{id}")
    public GenericResponseDTO<Object> getParameterById(@PathVariable String id) {
        GenericResponseDTO<Object> response = new GenericResponseDTO<>();
        try {
            CmsControlParameterDTO parameter = service.getParameterById(id);
            response.setStatus("Success");
            if(parameter == null){
                response.setMessage("Data not found with id: " + id);
            }else{
                response.setMessage("Success");
            }
            response.setData(parameter);
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching cms control Parameters by ID: {}", id, e);
            response.setStatus("Failure");
            response.setMessage("Data not found with id: " + id);
            response.setData(new HashMap<>());
        }
        return response;
    }

    @PostMapping
    public GenericResponseDTO<Object> createParameter(@RequestBody CmsControlParameterDTO parameterDTO) {
        GenericResponseDTO<Object> response = new GenericResponseDTO<>();
        try {
            CmsControlParameterDTO createdParameter = service.createParameter(parameterDTO);
            response.setStatus("Success");
            response.setMessage("Success");
            response.setData(createdParameter);
        } catch (DuplicateEntryException e) {
            LOGGER.error("Duplicate entry error occurred while creating cms control Parameters", e);
            response.setStatus("Failure");
            response.setMessage("Duplicate entry");
            response.setData(null);
        } catch (Exception e) {
            LOGGER.error("Error occurred while creating cms control Parameters", e);
            response.setStatus("Failure");
            response.setMessage("Something went wrong");
            response.setData(null);
        }
        return response;
    }

    @PutMapping("/{id}")
    public GenericResponseDTO<Object> updateParameter(@PathVariable String id, @RequestBody CmsControlParameterDTO parameterDTO) {
        GenericResponseDTO<Object> response = new GenericResponseDTO<>();
        try {
            CmsControlParameterDTO updatedParameter = service.updateParameter(id, parameterDTO);
            response.setStatus("Success");
            if(updatedParameter == null){
                response.setMessage("No data present");
                response.setData(null);
            }
            else {
                response.setMessage("data updated successfully");
                response.setData(updatedParameter);
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while updating cms control Parameters by ID: {}", id, e);
            response.setStatus("Failure");
            response.setMessage("Something went wrong");
            response.setData(null);
        }
        return response;
    }

    @DeleteMapping("/{id}")
    public GenericResponseDTO<Object> deleteParameter(@PathVariable String id) {
        GenericResponseDTO<Object> response = new GenericResponseDTO<>();
        try {
            boolean deletionSuccessful = service.deleteParameter(id);
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