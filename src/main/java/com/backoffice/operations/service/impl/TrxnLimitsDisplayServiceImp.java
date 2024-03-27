package com.backoffice.operations.service.impl;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.AnnexureTransferLimits;
import com.backoffice.operations.entity.AnnexureTransferWithSubLimits;
import com.backoffice.operations.entity.UserLimitTrxnEntity;
import com.backoffice.operations.enums.CustomerType;
import com.backoffice.operations.enums.TransferType;
import com.backoffice.operations.payloads.TexnLimitsDisplayDto;
import com.backoffice.operations.payloads.TexnLimitsDisplayDto.ResquestDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.AnnexureTransferLimitsRepo;
import com.backoffice.operations.repository.AnnexureTransferSubLimitsRepo;
import com.backoffice.operations.repository.UserLimitTrxnEntityRepo;
import com.backoffice.operations.service.TrxnLimitsDisplayService;

@Service
public class TrxnLimitsDisplayServiceImp implements TrxnLimitsDisplayService {
	private final static Logger logger = LoggerFactory.getLogger(TrxnLimitsDisplayServiceImp.class);

	@Autowired
	private AnnexureTransferSubLimitsRepo annexureTransferSubLimitsRepo;
	@Autowired
	private AnnexureTransferLimitsRepo annexureTransferLimitsRepo;
	@Autowired
	private UserLimitTrxnEntityRepo userLimitTrxnEntityRepo;

	@Override
	public GenericResponseDTO<Object> getTransferLimit(ResquestDTO requestDTO) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<>();
		try {
			TransferType transferTypeEnum = TransferType.fromString(requestDTO.getTransactionType());
			String globalLimit = transferTypeEnum.getCategoryAndDescription(requestDTO.getTransactionType())[0];
			String subType = transferTypeEnum.getCategoryAndDescription(requestDTO.getTransactionType())[1];
			CustomerType custType = CustomerType.fromString(requestDTO.getCtype());
			String segment = custType.getSegment();
			AnnexureTransferLimits annexureTransferLimits = annexureTransferLimitsRepo
					.findBySegmentAndGlobalLimit(segment, globalLimit);
			AnnexureTransferWithSubLimits annexureTransferWithSubLimits = annexureTransferSubLimitsRepo
					.findByAnnexureTransferLimitsAndSubTypeLimit(annexureTransferLimits, subType);
			UserLimitTrxnEntity userLimitTrxnEntity = userLimitTrxnEntityRepo
					.findByUniqueKey(requestDTO.getUniqueKey());

			TexnLimitsDisplayDto.ResponseDTO responseData = new TexnLimitsDisplayDto.ResponseDTO();
			responseData.setMin_amount_per_trans(annexureTransferWithSubLimits.getMinPerTrxnAmt());
			responseData.setMax_amount_per_trans(annexureTransferWithSubLimits.getMaxPerTrxnAmt());
			responseData.setDaily_amount(ammountDetailsStore((double) annexureTransferWithSubLimits.getDailyAmt(),
					userLimitTrxnEntity.getDailyTrxnLimit()));
			responseData.setMonthly_amount(ammountDetailsStore((double) annexureTransferWithSubLimits.getMonthlyAmt(),
					userLimitTrxnEntity.getMonthlyTrxnLimit()));
			responseData.setDaily_count(countDetails(annexureTransferWithSubLimits.getDailyCount(),
					(long) userLimitTrxnEntity.getDailyTrxnCount()));
			responseData.setMonthly_count(countDetails(annexureTransferWithSubLimits.getMonthlyCount(),
					(long) userLimitTrxnEntity.getMonthlyTrxnCount()));

			response.setMessage("Success");
			response.setStatus("Success");
			response.setData(responseData);
		} catch (Exception e) {
			logger.error("Error on getTransferLimit : {}", e.getMessage());
			response.setStatus("Falure");
			response.setMessage("Something Went Wrong");
			response.setData(new HashMap<>());
		}
		return response;
	}

	private TexnLimitsDisplayDto.AmmountDetails ammountDetailsStore(Double total, Double uses) {
		TexnLimitsDisplayDto.AmmountDetails result = new TexnLimitsDisplayDto.AmmountDetails();
		result.setTotal(total);
		result.setUsed(uses);
		result.setAvailable(total - uses < 0 ? 0 : total - uses);

		return result;
	}

	private TexnLimitsDisplayDto.CountDetails countDetails(Long total, Long uses) {
		TexnLimitsDisplayDto.CountDetails result = new TexnLimitsDisplayDto.CountDetails();
		result.setTotal(total);
		result.setUsed(uses);
		result.setAvailable(total - uses < 0 ? 0 : total - uses);

		return result;
	}
}
