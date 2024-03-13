package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.CmsControlParameter;
import com.backoffice.operations.entity.TransferParameter;
import com.backoffice.operations.exceptions.DuplicateEntryException;
import com.backoffice.operations.payloads.CmsControlParameterDTO;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.backoffice.operations.repository.CmsControlParameterRepository;
import com.backoffice.operations.service.CmsControlParameterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class CmsControlParameterServiceImpl implements CmsControlParameterService {

    private final CmsControlParameterRepository repository;
    private static final Logger LOGGER = LoggerFactory.getLogger(CmsControlParameterServiceImpl.class);

    @Autowired
    public CmsControlParameterServiceImpl(CmsControlParameterRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CmsControlParameterDTO> getAllParameters() {
        return repository.findAll().stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CmsControlParameterDTO getParameterById(String id) {
        return repository.findById(id)
                .map(this::mapEntityToDTO)
                .orElse(null);
    }

    @Override
    public CmsControlParameterDTO createParameter(CmsControlParameterDTO parameterDTO) {
        try {
            CmsControlParameter parameter = mapDTOToEntity(parameterDTO);
            return mapEntityToDTO(repository.save(parameter));
        }catch (DataIntegrityViolationException e){
            LOGGER.error("Error occurred while saving cms control parameter", e);
            throw new DuplicateEntryException("Duplicate entry");
        }catch (Exception e) {
            LOGGER.error("Error occurred while saving cms control parameter", e);
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public CmsControlParameterDTO updateParameter(String id, CmsControlParameterDTO parameterDTO) {
        Optional<CmsControlParameter> existingParameterOptional = repository.findById(id);
        if (existingParameterOptional.isPresent()) {
            CmsControlParameter existingParameter = existingParameterOptional.get();
            existingParameter.setVariable(parameterDTO.getVariable());
            existingParameter.setValue(parameterDTO.getValue());
            return mapEntityToDTO(repository.save(existingParameter));
        }
        return null; // Handle not found case
    }

    @Override
    public boolean deleteParameter(String id) {
        try {
            Optional<CmsControlParameter> existingParameterOptional = repository.findById(id);
            if (existingParameterOptional.isPresent()) {
                repository.deleteById(id);
                return true;
            } else {
                LOGGER.warn("No cms control parameter found with ID: {}", id);
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while deleting cms control parameter by ID: {}", id, e);
            return false;
        }
    }

    private CmsControlParameterDTO mapEntityToDTO(CmsControlParameter parameter) {
        return CmsControlParameterDTO.builder()
                .id(parameter.getId())
                .variable(parameter.getVariable())
                .value(parameter.getValue())
                .build();
    }

    private CmsControlParameter mapDTOToEntity(CmsControlParameterDTO parameterDTO) {
        return CmsControlParameter.builder()
                .id(parameterDTO.getId())
                .variable(parameterDTO.getVariable())
                .value(parameterDTO.getValue())
                .build();
    }
}