package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.CmsControlParameter;
import com.backoffice.operations.payloads.CmsControlParameterDTO;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.backoffice.operations.repository.CmsControlParameterRepository;
import com.backoffice.operations.service.CmsControlParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CmsControlParameterServiceImpl implements CmsControlParameterService {

    private final CmsControlParameterRepository repository;

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
        CmsControlParameter parameter = mapDTOToEntity(parameterDTO);
        return mapEntityToDTO(repository.save(parameter));
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
    public void deleteParameter(String id) {
        repository.deleteById(id);
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