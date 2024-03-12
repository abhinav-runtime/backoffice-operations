package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.TransferParameter;
import com.backoffice.operations.payloads.TransferParameterDTO;
import com.backoffice.operations.repository.TransferParameterRepository;
import com.backoffice.operations.service.TransferParameterService;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

@Service
public class TransferParameterServiceImpl implements TransferParameterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferParameterServiceImpl.class);

    @Autowired
    private TransferParameterRepository transferParameterRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TransferParameterDTO findById(String id) {
        try {
            Optional<TransferParameter> optionalTransferParameter = transferParameterRepository.findById(id);
            if (optionalTransferParameter.isPresent()) {
                return convertToDTO(optionalTransferParameter.get());
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching TransferParameter by ID: {}", id, e);
        }
        return null;
    }

    @Override
    public List<TransferParameterDTO> findAll() {
        try {
            List<TransferParameter> transferParameters = transferParameterRepository.findAll();
            return transferParameters.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching all TransferParameters", e);
            return null;
        }
    }

    @Override
    public TransferParameterDTO save(TransferParameterDTO transferParameterDTO) {
        try {
            TransferParameter transferParameter = convertToEntity(transferParameterDTO);
            TransferParameter savedTransferParameter = transferParameterRepository.save(transferParameter);
            return convertToDTO(savedTransferParameter);
        } catch (Exception e) {
            LOGGER.error("Error occurred while saving TransferParameter", e);
            return null;
        }
    }

    @Override
    public TransferParameterDTO update(String id, TransferParameterDTO updatedTransferParameterDTO) {
        try {
            Optional<TransferParameter> optionalTransferParameter = transferParameterRepository.findById(id);
            if (optionalTransferParameter.isPresent()) {
                TransferParameter existingTransferParameter = optionalTransferParameter.get();
                existingTransferParameter.setVariable(updatedTransferParameterDTO.getVariable());
                existingTransferParameter.setValue(updatedTransferParameterDTO.getValue());
                TransferParameter updatedEntity = transferParameterRepository.save(existingTransferParameter);
                return convertToDTO(updatedEntity);
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while updating TransferParameter by ID: {}", id, e);
        }
        return null;
    }

    @Override
    public void delete(String id) {
        try {
            transferParameterRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.error("Error occurred while deleting TransferParameter by ID: {}", id, e);
        }
    }

    private TransferParameterDTO convertToDTO(TransferParameter transferParameter) {
        return modelMapper.map(transferParameter, TransferParameterDTO.class);
    }

    private TransferParameter convertToEntity(TransferParameterDTO transferParameterDTO) {
        return modelMapper.map(transferParameterDTO, TransferParameter.class);
    }
}