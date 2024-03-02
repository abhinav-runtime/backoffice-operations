package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.Purpose;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.PurposeRepository;
import com.backoffice.operations.service.PurposeService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PurposeServiceImpl implements PurposeService {

    private final PurposeRepository purposeRepository;

    public PurposeServiceImpl(PurposeRepository purposeRepository) {
        this.purposeRepository = purposeRepository;
    }

    @Override
    public GenericResponseDTO<Object> getPurposeList() {
        List<Purpose> purposeList = purposeRepository.findAll();

        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
        Map<String, Object> data = new HashMap<>();
        data.put("purposeList", purposeList);
        responseDTO.setStatus("Success");
        responseDTO.setMessage("Success");
        responseDTO.setData(data);
        return responseDTO;
    }
}
