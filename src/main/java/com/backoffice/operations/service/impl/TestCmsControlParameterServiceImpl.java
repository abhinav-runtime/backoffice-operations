package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.TestCmsControlParameter;
import com.backoffice.operations.payloads.TestCmsControlParameterDTO;
import com.backoffice.operations.repository.TestCmsControlParameterRepository;
import com.backoffice.operations.service.TestCmsControlParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class TestCmsControlParameterServiceImpl implements TestCmsControlParameterService {

    private final TestCmsControlParameterRepository repository;

    @Autowired
    public TestCmsControlParameterServiceImpl(TestCmsControlParameterRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TestCmsControlParameterDTO> getAllParameters() {
        return repository.findAll().stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TestCmsControlParameterDTO getParameterById(String id) {
        return repository.findById(id)
                .map(this::mapEntityToDTO)
                .orElse(null);
    }

    @Override
    public TestCmsControlParameterDTO createParameter(TestCmsControlParameterDTO parameterDTO) {
        TestCmsControlParameter parameter = mapDTOToEntity(parameterDTO);
        return mapEntityToDTO(repository.save(parameter));
    }

    @Override
    public TestCmsControlParameterDTO updateParameter(String id, TestCmsControlParameterDTO parameterDTO) {
        Optional<TestCmsControlParameter> existingParameterOptional = repository.findById(id);
        if (existingParameterOptional.isPresent()) {
            TestCmsControlParameter existingParameter = existingParameterOptional.get();
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

    private TestCmsControlParameterDTO mapEntityToDTO(TestCmsControlParameter parameter) {
        return TestCmsControlParameterDTO.builder()
                .id(parameter.getId())
                .variable(parameter.getVariable())
                .value(parameter.getValue())
                .build();
    }

    private TestCmsControlParameter mapDTOToEntity(TestCmsControlParameterDTO parameterDTO) {
        return TestCmsControlParameter.builder()
                .id(parameterDTO.getId())
                .variable(parameterDTO.getVariable())
                .value(parameterDTO.getValue())
                .build();
    }
}