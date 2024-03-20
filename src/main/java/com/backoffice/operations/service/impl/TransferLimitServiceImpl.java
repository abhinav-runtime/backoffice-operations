package com.backoffice.operations.service.impl;

import com.backoffice.operations.entity.AnnexureTransferLimits;
import com.backoffice.operations.entity.AnnexureTransferWithSubLimits;
import com.backoffice.operations.entity.UserLimitTrxnEntity;
import com.backoffice.operations.enums.CustomerType;
import com.backoffice.operations.enums.TransferType;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.AnnexureTransferLimitsRepo;
import com.backoffice.operations.repository.AnnexureTransferSubLimitsRepo;
import com.backoffice.operations.repository.UserLimitTrxnEntityRepo;
import com.backoffice.operations.service.TransferLimitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class TransferLimitServiceImpl implements TransferLimitService {

    private final AnnexureTransferLimitsRepo annexureTransferLimitsRepo;

    private final AnnexureTransferSubLimitsRepo annexureTransferSubLimitsRepo;

    private final UserLimitTrxnEntityRepo userLimitTrxnEntityRepo;

    private static final Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);

    public TransferLimitServiceImpl(AnnexureTransferLimitsRepo annexureTransferLimitsRepo, AnnexureTransferSubLimitsRepo annexureTransferSubLimitsRepo, UserLimitTrxnEntityRepo userLimitTrxnEntityRepo) {
        this.annexureTransferLimitsRepo = annexureTransferLimitsRepo;
        this.annexureTransferSubLimitsRepo = annexureTransferSubLimitsRepo;
        this.userLimitTrxnEntityRepo = userLimitTrxnEntityRepo;
    }

    @Override
    public GenericResponseDTO<Object> getTransferLimit(String customerType, String uniqueKey, String transactionType, Double transactionAmt) {
        logger.info("In TransferLimitServiceImpl getTransferLimit");
        GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
        Map<String, Object> dataMap = new HashMap<>();
        try {

            String segment;
            String globalType;
            String subType;
            segment = Objects.requireNonNull(CustomerType.fromString(customerType)).getSegment();

            TransferType transferType = TransferType.fromString(transactionType);
            globalType = transferType.getGlobalType();
            subType = transferType.getSubType();

            AnnexureTransferLimits annexureTransferLimits = annexureTransferLimitsRepo.findBySegmentAndGlobalLimit(segment, globalType);

            AnnexureTransferWithSubLimits annexureTransferWithSubLimits = annexureTransferSubLimitsRepo.findByAnnexureTransferLimitsAndSubTypeLimit(annexureTransferLimits, subType);

            long dailyAmt = annexureTransferWithSubLimits.getDailyAmt();
            long dailyCount = annexureTransferWithSubLimits.getDailyCount();
            long maxPerTrxnAmt = annexureTransferWithSubLimits.getMaxPerTrxnAmt();
            long minPerTrxnAmt = annexureTransferWithSubLimits.getMinPerTrxnAmt();
            long monthlyAmt = annexureTransferWithSubLimits.getMonthlyAmt();
            long monthlyCount = annexureTransferWithSubLimits.getMonthlyCount();
            long globalDailyAmt = annexureTransferWithSubLimits.getAnnexureTransferLimits().getDailyAmt();
            long globalDailyCount = annexureTransferWithSubLimits.getAnnexureTransferLimits().getDailyCount();
            long globalMaxPerTrxnAmt = annexureTransferWithSubLimits.getAnnexureTransferLimits().getMaxPerTrxnAmt();
            long globalMinPerTrxnAmt = annexureTransferWithSubLimits.getAnnexureTransferLimits().getMinPerTrxnAmt();
            long globalMonthlyAmt = annexureTransferWithSubLimits.getAnnexureTransferLimits().getMonthlyAmt();
            long globalMonthlyCount = annexureTransferWithSubLimits.getAnnexureTransferLimits().getMonthlyCount();


            UserLimitTrxnEntity userLimitTrxnEntity = userLimitTrxnEntityRepo.findByUniqueKey(uniqueKey);
            if (transactionAmt >= minPerTrxnAmt) {
                if (transactionAmt <= maxPerTrxnAmt) {
                    if (userLimitTrxnEntity.getMonthlyTrxnCount() < monthlyCount) {
                        if (userLimitTrxnEntity.getMonthlyTrxnLimit() < monthlyAmt
                                && userLimitTrxnEntity.getMonthlyTrxnLimit() + transactionAmt < monthlyAmt) {
                            if (userLimitTrxnEntity.getDailyTrxnCount() < dailyCount) {
                                if (userLimitTrxnEntity.getDailyTrxnLimit() < dailyAmt
                                        && userLimitTrxnEntity.getDailyTrxnLimit() + transactionAmt < dailyAmt) {
                                    dataMap.put("isTrxnAllowed", true);
                                } else {
                                    dataMap.put("isTrxnAllowed", false);
                                    dataMap.put("message", "User daily transaction limit exceeded");
                                }
                            } else {
                                dataMap.put("isTrxnAllowed", false);
                                dataMap.put("message", "User daily transaction count exceeded");
                            }
                        } else {
                            dataMap.put("isTrxnAllowed", false);
                            dataMap.put("message", "User monthly transaction limit exceeded");
                        }
                    } else {
                        dataMap.put("isTrxnAllowed", false);
                        dataMap.put("message", "User monthly transaction count exceeded");
                    }
                } else {
                    dataMap.put("isTrxnAllowed", false);
                    dataMap.put("message", "Max limit per transaction exceeded");
                }
            } else {
                dataMap.put("isTrxnAllowed", false);
                dataMap.put("message", "Min limit per transaction to proceed is less");
            }
            dataMap.put("uniqueKey", uniqueKey);
            responseDTO.setData(dataMap);
            responseDTO.setStatus("Success");
            responseDTO.setMessage("Success");

            return responseDTO;
        } catch (Exception e) {
            logger.error("Error in TransferLimitServiceImpl getTransferLimit {}", e.getMessage());
            dataMap.put("uniqueKey", uniqueKey);
            responseDTO.setData(dataMap);
            responseDTO.setStatus("Failure");
            responseDTO.setMessage("Validation Failure");
            return responseDTO;

        }
    }

    @Override
    public void saveUserTrxnLimitData(String uniqueKey, Double dailyTrxnAmt, String accountNumber) {
        UserLimitTrxnEntity userLimitTrxnEntityObject = userLimitTrxnEntityRepo.findByUniqueKey("");
        if (Objects.isNull(userLimitTrxnEntityObject)) {
            UserLimitTrxnEntity userLimitTrxnEntity = UserLimitTrxnEntity.builder()
                    .uniqueKey(uniqueKey).dailyTrxnLimit(dailyTrxnAmt).dailyTrxnCount(1)
                    .monthlyTrxnLimit(dailyTrxnAmt).monthlyTrxnCount(1).accountNumber(accountNumber).build();
        } else {
            userLimitTrxnEntityObject.setDailyTrxnLimit(userLimitTrxnEntityObject.getDailyTrxnLimit() + dailyTrxnAmt);
            userLimitTrxnEntityObject.setDailyTrxnCount(userLimitTrxnEntityObject.getDailyTrxnCount() + 1);
            userLimitTrxnEntityObject.setMonthlyTrxnLimit(userLimitTrxnEntityObject.getMonthlyTrxnLimit() + dailyTrxnAmt);
            userLimitTrxnEntityObject.setMonthlyTrxnCount(userLimitTrxnEntityObject.getMonthlyTrxnCount() + 1);
        }
        userLimitTrxnEntityRepo.save(userLimitTrxnEntityObject);
    }
}
