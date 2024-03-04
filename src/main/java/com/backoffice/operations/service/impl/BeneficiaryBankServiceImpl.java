package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.BeneficiaryBank;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.BeneficiaryBankRepository;
import com.backoffice.operations.service.BeneficiaryBankService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BeneficiaryBankServiceImpl implements BeneficiaryBankService {

    private final BeneficiaryBankRepository beneficiaryBankRepository;

    public BeneficiaryBankServiceImpl(BeneficiaryBankRepository beneficiaryBankRepository) {
        this.beneficiaryBankRepository = beneficiaryBankRepository;
    }

    @Override
    public GenericResponseDTO<Object> getBeneficiaryBankList() {
        List<BeneficiaryBank> beneficiaryBankList = beneficiaryBankRepository.findAll();

        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
        Map<String, Object> data = new HashMap<>();
        data.put("beneficiaryBankList", beneficiaryBankList);
        responseDTO.setStatus("Success");
        responseDTO.setMessage("Success");
        responseDTO.setData(data);
        return responseDTO;
    }
}
