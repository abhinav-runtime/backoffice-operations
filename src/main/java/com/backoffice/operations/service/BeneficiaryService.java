package com.backoffice.operations.service;

import com.backoffice.operations.entity.Beneficiary;
import com.backoffice.operations.payloads.AlizzTransferDto;
import org.springframework.stereotype.Service;

@Service
public interface BeneficiaryService {

    Beneficiary addBeneficiary(AlizzTransferDto.Receiver receiver);
}
