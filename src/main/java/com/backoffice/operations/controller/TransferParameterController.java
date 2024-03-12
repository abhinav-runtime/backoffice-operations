package com.backoffice.operations.controller;
import com.backoffice.operations.payloads.TransferParameterDTO;
import com.backoffice.operations.service.TransferParameterService;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transfer-parameters")
public class TransferParameterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferParameterController.class);

    @Autowired
    private TransferParameterService transferParameterService;

    @GetMapping("/{id}")
    public ResponseEntity<TransferParameterDTO> getTransferParameterById(@PathVariable String id) {
        try {
            TransferParameterDTO transferParameterDTO = transferParameterService.findById(id);
            return (transferParameterDTO != null)
                    ? new ResponseEntity<>(transferParameterDTO, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching TransferParameter by ID: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<TransferParameterDTO>> getAllTransferParameters() {
        try {
            List<TransferParameterDTO> transferParameterDTOs = transferParameterService.findAll();
            return new ResponseEntity<>(transferParameterDTOs, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching all TransferParameters", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<TransferParameterDTO> createTransferParameter(
            @RequestBody TransferParameterDTO transferParameterDTO) {
        try {
            TransferParameterDTO createdTransferParameter = transferParameterService.save(transferParameterDTO);
            return new ResponseEntity<>(createdTransferParameter, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error("Error occurred while creating TransferParameter", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransferParameterDTO> updateTransferParameter(
            @PathVariable String id, @RequestBody TransferParameterDTO updatedTransferParameterDTO) {
        try {
            TransferParameterDTO updatedEntity = transferParameterService.update(id, updatedTransferParameterDTO);
            return (updatedEntity != null)
                    ? new ResponseEntity<>(updatedEntity, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error("Error occurred while updating TransferParameter by ID: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransferParameter(@PathVariable String id) {
        try {
            transferParameterService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            LOGGER.error("Error occurred while deleting TransferParameter by ID: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}