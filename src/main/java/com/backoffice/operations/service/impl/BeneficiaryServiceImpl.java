package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.Beneficiary;
import com.backoffice.operations.payloads.AlizzTransferDto;
import com.backoffice.operations.repository.BeneficiaryRepository;
import com.backoffice.operations.service.BeneficiaryService;
import org.springframework.stereotype.Service;

@Service
public class BeneficiaryServiceImpl implements BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryServiceImpl(BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
    }

    @Override
    public Beneficiary addBeneficiary(AlizzTransferDto.Receiver receiver) {
        Beneficiary beneficiary = Beneficiary.builder()
                .accountName(receiver.getAccountName())
                .accountNumber(receiver.getAccountNumber())
                .notesToReceiver(receiver.getNotesToReceiver()).build();

        return beneficiaryRepository.save(beneficiary);
    }
}
