package com.backoffice.operations.service;

import com.backoffice.operations.entity.TransferParameter;
import com.backoffice.operations.payloads.TransferParameterDTO;

import java.util.List;

public interface TransferParameterService {
    TransferParameterDTO findById(String id);

    List<TransferParameterDTO> findAll();

    TransferParameterDTO save(TransferParameterDTO transferParameterDTO);

    TransferParameterDTO update(String id, TransferParameterDTO updatedTransferParameterDTO);

    boolean delete(String id);
}
