package com.backoffice.operations.service;

import com.backoffice.operations.entity.TransferParameter;
import com.backoffice.operations.entity.TransfersParameter;
import com.backoffice.operations.payloads.TransferParameterDTO;
import com.backoffice.operations.payloads.TransfersParameterDTO;

import java.util.List;

public interface TransferParameterService {
    TransfersParameterDTO findById(Long id);

    List<TransfersParameterDTO> findAll();

    TransfersParameterDTO create(TransfersParameterDTO transferParameterDTO);

    TransfersParameterDTO update(Long id, TransfersParameterDTO transferParameterDTO);

    void delete(Long id);
}
