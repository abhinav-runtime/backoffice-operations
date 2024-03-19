package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.AnnexureTransferLimits;
import com.backoffice.operations.entity.AnnexureTransferWithSubLimits;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.AnnexureTransferLimitsRepo;
import com.backoffice.operations.repository.AnnexureTransferSubLimitsRepo;
import com.backoffice.operations.service.TransferLimitService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferLimitServiceImpl implements TransferLimitService {

    private final AnnexureTransferLimitsRepo annexureTransferLimitsRepo;

    private final AnnexureTransferSubLimitsRepo annexureTransferSubLimitsRepo;

    public TransferLimitServiceImpl(AnnexureTransferLimitsRepo annexureTransferLimitsRepo, AnnexureTransferSubLimitsRepo annexureTransferSubLimitsRepo) {
        this.annexureTransferLimitsRepo = annexureTransferLimitsRepo;
        this.annexureTransferSubLimitsRepo = annexureTransferSubLimitsRepo;
    }

    @Override
    public GenericResponseDTO<Object> getTransferLimit(String customerType, String uniqueKey, String transactionType) {
        String segment;
        String globalLimit = null;
        if(customerType.equalsIgnoreCase("Individual")){
            segment = "Mass - General";
        }else {
            segment = "Tharwa";
        }

        if (transactionType.equalsIgnoreCase("WithinAlizz") || transactionType.equalsIgnoreCase("SelfTransfer")){
            globalLimit = "Own (within own account)";
        }else if (transactionType.equalsIgnoreCase("ach")){
            globalLimit = "Domestic";
        }
        AnnexureTransferLimits annexureTransferLimits = annexureTransferLimitsRepo.findBySegmentAndGlobalLimit(segment,globalLimit);

        List<AnnexureTransferWithSubLimits> annexureTransferWithSubLimitsList = annexureTransferSubLimitsRepo.findByAnnexureTransferLimits(annexureTransferLimits);

        AnnexureTransferWithSubLimits annexureTransferWithSubLimits = annexureTransferWithSubLimitsList.get(0);


        return null;
    }
}
