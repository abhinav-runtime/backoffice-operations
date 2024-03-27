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

            if (Objects.nonNull(annexureTransferLimits)) {
                AnnexureTransferWithSubLimits annexureTransferWithSubLimits = annexureTransferSubLimitsRepo.findByAnnexureTransferLimitsAndSubTypeLimit(annexureTransferLimits, subType);
                if (Objects.nonNull(annexureTransferWithSubLimits)) {
                    long dailyAmt = Objects.nonNull(annexureTransferWithSubLimits.getDailyAmt()) ? annexureTransferWithSubLimits.getDailyAmt() : 0;
                    long dailyCount = Objects.nonNull(annexureTransferWithSubLimits.getDailyCount()) ? annexureTransferWithSubLimits.getDailyCount() : 0;
                    long maxPerTrxnAmt = Objects.nonNull(annexureTransferWithSubLimits.getMaxPerTrxnAmt()) ? annexureTransferWithSubLimits.getMaxPerTrxnAmt() : 0;
                    long minPerTrxnAmt = Objects.nonNull(annexureTransferWithSubLimits.getMinPerTrxnAmt()) ? annexureTransferWithSubLimits.getMinPerTrxnAmt() : 0;
                    long monthlyAmt = Objects.nonNull(annexureTransferWithSubLimits.getMonthlyAmt()) ? annexureTransferWithSubLimits.getMonthlyAmt() : 0;
                    long monthlyCount = Objects.nonNull(annexureTransferWithSubLimits.getMonthlyCount()) ? annexureTransferWithSubLimits.getMonthlyCount() : 0;
                    long globalDailyAmt = Objects.nonNull(annexureTransferWithSubLimits.getAnnexureTransferLimits()) &&
                            Objects.nonNull(annexureTransferWithSubLimits.getAnnexureTransferLimits().getDailyAmt()) ?
                            annexureTransferWithSubLimits.getAnnexureTransferLimits().getDailyAmt() : 0;
                    long globalDailyCount = Objects.nonNull(annexureTransferWithSubLimits.getAnnexureTransferLimits()) &&
                            Objects.nonNull(annexureTransferWithSubLimits.getAnnexureTransferLimits().getDailyCount()) ?
                            annexureTransferWithSubLimits.getAnnexureTransferLimits().getDailyCount() : 0;
                    long globalMaxPerTrxnAmt = Objects.nonNull(annexureTransferWithSubLimits.getAnnexureTransferLimits()) &&
                            Objects.nonNull(annexureTransferWithSubLimits.getAnnexureTransferLimits().getMaxPerTrxnAmt()) ?
                            annexureTransferWithSubLimits.getAnnexureTransferLimits().getMaxPerTrxnAmt() : 0;
                    long globalMinPerTrxnAmt = Objects.nonNull(annexureTransferWithSubLimits.getAnnexureTransferLimits()) &&
                            Objects.nonNull(annexureTransferWithSubLimits.getAnnexureTransferLimits().getMinPerTrxnAmt()) ?
                            annexureTransferWithSubLimits.getAnnexureTransferLimits().getMinPerTrxnAmt() : 0;
                    long globalMonthlyAmt = Objects.nonNull(annexureTransferWithSubLimits.getAnnexureTransferLimits()) &&
                            Objects.nonNull(annexureTransferWithSubLimits.getAnnexureTransferLimits().getMonthlyAmt()) ?
                            annexureTransferWithSubLimits.getAnnexureTransferLimits().getMonthlyAmt() : 0;
                    long globalMonthlyCount = Objects.nonNull(annexureTransferWithSubLimits.getAnnexureTransferLimits()) &&
                            Objects.nonNull(annexureTransferWithSubLimits.getAnnexureTransferLimits().getMonthlyCount()) ?
                            annexureTransferWithSubLimits.getAnnexureTransferLimits().getMonthlyCount() : 0;


                    UserLimitTrxnEntity userLimitTrxnEntity = userLimitTrxnEntityRepo.findByUniqueKey(uniqueKey);
                    if (Objects.nonNull(userLimitTrxnEntity) && transactionAmt >= minPerTrxnAmt) {
                        if (transactionAmt <= maxPerTrxnAmt) {
                            Integer monthlyTrxnCount = Objects.nonNull(userLimitTrxnEntity.getMonthlyTrxnCount()) ? userLimitTrxnEntity.getMonthlyTrxnCount() : 0;
                            if (monthlyTrxnCount < monthlyCount) {
                                double monthlyTrxnLimit = Objects.nonNull(userLimitTrxnEntity.getMonthlyTrxnLimit()) ? userLimitTrxnEntity.getMonthlyTrxnLimit() : 0;
                                if (monthlyTrxnLimit < monthlyAmt
                                        && monthlyTrxnLimit + transactionAmt < monthlyAmt) {
                                    double dailyTrxnCount = Objects.nonNull(userLimitTrxnEntity.getDailyTrxnCount()) ? userLimitTrxnEntity.getDailyTrxnCount() : 0;
                                    if (dailyTrxnCount < dailyCount) {
                                        if (dailyTrxnCount < dailyAmt
                                                && dailyTrxnCount + transactionAmt < dailyAmt) {
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
                }else {
                    dataMap.put("uniqueKey", uniqueKey);
                    dataMap.put("isTrxnAllowed", false);
                    responseDTO.setData(dataMap);
                    responseDTO.setStatus("Failure");
                    responseDTO.setMessage("User Not Found");
                    return responseDTO;
                }
            } else {
                dataMap.put("uniqueKey", uniqueKey);
                dataMap.put("isTrxnAllowed", false);
                responseDTO.setData(dataMap);
                responseDTO.setStatus("Failure");
                responseDTO.setMessage("User Not Found");
                return responseDTO;
            }
        } catch (Exception e) {
            logger.error("Error in TransferLimitServiceImpl getTransferLimit {}", e.getMessage());
            dataMap.put("uniqueKey", uniqueKey);
            dataMap.put("isTrxnAllowed", false);
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
