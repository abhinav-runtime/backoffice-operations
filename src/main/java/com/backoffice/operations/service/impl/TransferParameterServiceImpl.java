package com.backoffice.operations.service.impl;
import com.backoffice.operations.entity.TransfersParameter;
import com.backoffice.operations.payloads.TransfersParameterDTO;
import com.backoffice.operations.repository.TransferParameterRepository;
import com.backoffice.operations.service.TransferParameterService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransferParameterServiceImpl implements TransferParameterService {

    @Autowired
    private TransferParameterRepository transfersParameterRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TransfersParameterDTO findById(Long id) {
        Optional<TransfersParameter> transfersParameterOptional = transfersParameterRepository.findById(id);
        return transfersParameterOptional.map(transfersParameter -> modelMapper.map(transfersParameter, TransfersParameterDTO.class)).orElse(null);
    }

    @Override
    public List<TransfersParameterDTO> findAll() {
        List<TransfersParameter> transfersParameters = transfersParameterRepository.findAll();
        return transfersParameters.stream()
                .map(transferParameter -> modelMapper.map(transferParameter, TransfersParameterDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public TransfersParameterDTO create(TransfersParameterDTO transferParameterDTO) {
        TransfersParameter transfersParameter = modelMapper.map(transferParameterDTO, TransfersParameter.class);
        TransfersParameter savedTransferParameter = transfersParameterRepository.save(transfersParameter);
        return modelMapper.map(savedTransferParameter, TransfersParameterDTO.class);
    }

    @Override
    public TransfersParameterDTO update(Long id, TransfersParameterDTO transferParameterDTO) {
        Optional<TransfersParameter> existingTransferParameterOptional = transfersParameterRepository.findById(id);
        if (existingTransferParameterOptional.isPresent()) {
            TransfersParameter existingTransferParameter = existingTransferParameterOptional.get();
            // Configure ModelMapper to only map non-null fields from DTO to entity
            modelMapper.map(transferParameterDTO, existingTransferParameter);
            // Save the updated entity
            TransfersParameter updatedTransferParameter = transfersParameterRepository.save(existingTransferParameter);
            return modelMapper.map(updatedTransferParameter, TransfersParameterDTO.class);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        transfersParameterRepository.deleteById(id);
    }
}