package com.backoffice.operations.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.AnnexureTransferLimits;
import com.backoffice.operations.entity.AnnexureTransferWithSubLimits;
import com.backoffice.operations.payloads.AnnexureTransferLimitsDTO;
import com.backoffice.operations.payloads.AnnexureTransferWithSubLimitsDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.AnnexureTransferLimitsRepo;
import com.backoffice.operations.repository.AnnexureTransferSubLimitsRepo;
import com.backoffice.operations.service.AnnexureTransferLimitService;

@Service
public class AnnexureTransferLimitServiceImp implements AnnexureTransferLimitService {
	private static final Logger logger = LoggerFactory.getLogger(AnnexureTransferLimitServiceImp.class);
	ModelMapper modelMapper = new ModelMapper();
	@Autowired
	private AnnexureTransferLimitsRepo annexureTransferLimitsRepo;
	@Autowired
	private AnnexureTransferSubLimitsRepo annexureTransferSubLimitsRepo;

	@Override
	public GenericResponseDTO<Object> getAllAnnexureTransferLimits() {
		GenericResponseDTO<Object> resposeDto = new GenericResponseDTO<Object>();
		List<AnnexureTransferLimitsDTO> annexureTransferLimits = new ArrayList<>();
		try {
			annexureTransferLimitsRepo.findAll().forEach(element -> {
				AnnexureTransferLimitsDTO annexureTransferLimitsDTO = modelMapper.map(element,
						AnnexureTransferLimitsDTO.class);
				annexureTransferLimits.add(annexureTransferLimitsDTO);
			});
			if (annexureTransferLimits.size() > 0) {
				resposeDto.setData(annexureTransferLimits);
				resposeDto.setStatus("Sucess");
				resposeDto.setMessage("Annexure Transfer Limits Retrieved Successfully");
				return resposeDto;
			} else {
				resposeDto.setData(new HashMap<>());
				resposeDto.setStatus("Failure");
				resposeDto.setMessage("Annexure Transfer Limits Not Found");
				return resposeDto;
			}
		} catch (Exception e) {
			logger.error("Error in getting Annexure Transfer Limits : {}", e.getMessage());
			resposeDto.setData(new HashMap<>());
			resposeDto.setStatus("Failure");
			resposeDto.setMessage("Something went wrong.");
			return resposeDto;
		}
	}

	@Override
	public GenericResponseDTO<Object> getAnnexureTransferLimitsById(String id) {
		GenericResponseDTO<Object> resposeDto = new GenericResponseDTO<Object>();
		try {
			AnnexureTransferLimitsDTO annexureTransferLimitsDTO = modelMapper
					.map(annexureTransferLimitsRepo.findById(id).get(), AnnexureTransferLimitsDTO.class);
			if (annexureTransferLimitsDTO != null) {
				resposeDto.setData(annexureTransferLimitsDTO);
				resposeDto.setStatus("Sucess");
				resposeDto.setMessage("Annexure Transfer Limits Retrieved Successfully");
				return resposeDto;
			} else {
				resposeDto.setData(new HashMap<>());
				resposeDto.setStatus("Failure");
				resposeDto.setMessage("Annexure Transfer Limits Not Found");
				return resposeDto;
			}
		} catch (Exception e) {
			logger.error("Error in getting Annexure Transfer Limits : {}", e.getMessage());
			resposeDto.setData(new HashMap<>());
			resposeDto.setStatus("Failure");
			resposeDto.setMessage("Something went wrong.");
			return resposeDto;
		}
	}

	@Override
	public GenericResponseDTO<Object> createAnnexureTransferLimits(AnnexureTransferLimitsDTO annexureTransferLimits) {
		GenericResponseDTO<Object> resposeDto = new GenericResponseDTO<Object>();
		try {
			AnnexureTransferLimits annexureTransferLimitsEntity = modelMapper.map(annexureTransferLimits,
					AnnexureTransferLimits.class);
			annexureTransferLimitsEntity = annexureTransferLimitsRepo.save(annexureTransferLimitsEntity);
			resposeDto.setData(annexureTransferLimitsEntity);
			resposeDto.setStatus("Sucess");
			resposeDto.setMessage("Annexure Transfer Limits Created Successfully");
			return resposeDto;
		} catch (Exception e) {
			logger.error("Error in creating Annexure Transfer Limits : {}", e.getMessage());
			resposeDto.setData(new HashMap<>());
			resposeDto.setStatus("Failure");
			resposeDto.setMessage("Something went wrong.");
			return resposeDto;
		}
	}

	@Override
	public GenericResponseDTO<Object> updateAnnexureTransferLimits(String id,
			AnnexureTransferLimitsDTO annexureTransferLimits) {
		GenericResponseDTO<Object> resposeDto = new GenericResponseDTO<Object>();
		try {
			AnnexureTransferLimits annexureTransferLimitsEntity = annexureTransferLimitsRepo.findById(id).get();
			if (annexureTransferLimitsEntity == null) {
				resposeDto.setData(new HashMap<>());
				resposeDto.setStatus("Failure");
				resposeDto.setMessage("Annexure Transfer Limits Not Found");
				return resposeDto;
			}
			if (annexureTransferLimits.getGlobalLimit() != null || annexureTransferLimits.getGlobalLimit() != "") {
				annexureTransferLimitsEntity.setGlobalLimit(annexureTransferLimits.getGlobalLimit());
			}
			if (annexureTransferLimits.getSegment() != null || annexureTransferLimits.getSegment() != "") {
				annexureTransferLimitsEntity.setSegment(annexureTransferLimits.getSegment());
			}
			if (annexureTransferLimits.getDailyAmt() != 0) {
				annexureTransferLimitsEntity.setDailyAmt(annexureTransferLimits.getDailyAmt());
			}
			if (annexureTransferLimits.getDailyCount() != 0) {
				annexureTransferLimitsEntity.setDailyCount(annexureTransferLimits.getDailyCount());
			}
			if (annexureTransferLimits.getMaxPerTrxnAmt() != 0) {
				annexureTransferLimitsEntity.setMaxPerTrxnAmt(annexureTransferLimits.getMaxPerTrxnAmt());
			}
			if (annexureTransferLimits.getMinPerTrxnAmt() != 0) {
				annexureTransferLimitsEntity.setMinPerTrxnAmt(annexureTransferLimits.getMinPerTrxnAmt());
			}
			if (annexureTransferLimits.getMonthlyAmt() != 0) {
				annexureTransferLimitsEntity.setMonthlyAmt(annexureTransferLimits.getMonthlyAmt());
			}
			if (annexureTransferLimits.getMonthlyCount() != 0) {
				annexureTransferLimitsEntity.setMonthlyCount(annexureTransferLimits.getMonthlyCount());
			}
			annexureTransferLimitsEntity = annexureTransferLimitsRepo.save(annexureTransferLimitsEntity);
			resposeDto.setData(annexureTransferLimitsEntity);
			resposeDto.setStatus("Sucess");
			resposeDto.setMessage("Annexure Transfer Limits Updated Successfully");
			return resposeDto;
		} catch (Exception e) {
			logger.error("Error occur during execute updateAnnexureTransferLimits : {}", e.getMessage());
			resposeDto.setData(new HashMap<>());
			resposeDto.setStatus("Failure");
			resposeDto.setMessage("Something went wrong.");
			return resposeDto;
		}
	}

	@Override
	public GenericResponseDTO<Object> deleteAnnexureTransferLimits(String id) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<Object>();
		try {
			annexureTransferSubLimitsRepo.deleteAllInBatch(annexureTransferSubLimitsRepo
					.findByAnnexureTransferLimits(annexureTransferLimitsRepo.findById(id).get()));
			annexureTransferLimitsRepo.deleteById(id);
			response.setData(new HashMap<>());
			response.setStatus("Success");
			response.setMessage("Annexure Transfer Limits Deleted Successfully");
			return response;
		} catch (Exception e) {
			logger.error("Error occur during execute deleteAnnexureTransferLimits : {}", e.getMessage());
			response.setData(new HashMap<>());
			response.setStatus("Failure");
			response.setMessage("Something went wrong.");
			return response;
		}
	}

	@Override
	public GenericResponseDTO<Object> getAllAnnexureTransferWithSubLimits() {
		GenericResponseDTO<Object> respose = new GenericResponseDTO<Object>();
		List<AnnexureTransferWithSubLimitsDTO> responseData = new ArrayList<>();
		try {
			annexureTransferSubLimitsRepo.findAll().forEach(element -> {
				AnnexureTransferWithSubLimitsDTO annexureTransferWithSubLimitsDTO = modelMapper.map(element,
						AnnexureTransferWithSubLimitsDTO.class);
				responseData.add(annexureTransferWithSubLimitsDTO);
			});
			if (responseData.size() > 0) {
				respose.setData(responseData);
				respose.setStatus("Success");
				respose.setMessage("Annexure Transfer With Sub Limits Retrieved Successfully");
				return respose;
			} else {
				respose.setData(new HashMap<>());
				respose.setStatus("Failure");
				respose.setMessage("Annexure Transfer With Sub Limits Not Found");
				return respose;
			}
		} catch (Exception e) {
			logger.error("Error in getting Annexure Transfer With Sub Limits : {}", e.getMessage());
			respose.setData(new HashMap<>());
			respose.setStatus("Failure");
			respose.setMessage("Something went wrong.");
			return respose;
		}
	}

	@Override
	public GenericResponseDTO<Object> getAnnexureTransferWithSubLimitsById(String id) {
		GenericResponseDTO<Object> respose = new GenericResponseDTO<Object>();
		try {
			AnnexureTransferWithSubLimitsDTO annexureTransferWithSubLimitsDTO = modelMapper
					.map(annexureTransferSubLimitsRepo.findById(id).get(), AnnexureTransferWithSubLimitsDTO.class);
			if (annexureTransferWithSubLimitsDTO != null) {
				respose.setData(annexureTransferWithSubLimitsDTO);
				respose.setStatus("Success");
				respose.setMessage("Annexure Transfer With Sub Limits Retrieved Successfully");
				return respose;
			} else {
				respose.setData(new HashMap<>());
				respose.setStatus("Failure");
				respose.setMessage("Annexure Transfer With Sub Limits Not Found");
				return respose;
			}
		} catch (Exception e) {
			logger.error("Error occur during execute getAnnexureTransferWithSubLimitsById : {}", e.getMessage());
			respose.setData(new HashMap<>());
			respose.setStatus("Failure");
			respose.setMessage("Something went wrong.");
			return respose;
		}
	}

	@Override
	public GenericResponseDTO<Object> createAnnexureTransferWithSubLimits(
			AnnexureTransferWithSubLimitsDTO.AnnexureTransferWithSubLimitsRequestDTO annexureTransferWithSubLimits) {
		GenericResponseDTO<Object> respose = new GenericResponseDTO<Object>();
		try {
			AnnexureTransferWithSubLimits annrexureTransferWithSubLimitsEntity = modelMapper.map(annexureTransferWithSubLimits,
					AnnexureTransferWithSubLimits.class);
//			annrexureTransferWithSubLimitsEntity.setSubTypeLimit(annexureTransferWithSubLimits.getSubTypeLimit());
			annrexureTransferWithSubLimitsEntity.setAnnexureTransferLimits(annexureTransferLimitsRepo
					.findById(annexureTransferWithSubLimits.getAnnexureTransferLimitsId()).get());
			annrexureTransferWithSubLimitsEntity = annexureTransferSubLimitsRepo
					.save(annrexureTransferWithSubLimitsEntity);

			respose.setData(
					modelMapper.map(annrexureTransferWithSubLimitsEntity, AnnexureTransferWithSubLimitsDTO.class));
			respose.setStatus("Success");
			respose.setMessage("Annexure Transfer With Sub Limits Created Successfully");
			return respose;
		} catch (Exception e) {
			logger.error("Error occur during execute createAnnexureTransferWithSubLimits : {}", e.getMessage());
			respose.setData(new HashMap<>());
			respose.setStatus("Failure");
			respose.setMessage("Something went wrong.");
			return respose;
		}
	}

	@Override
	public GenericResponseDTO<Object> updateAnnexureTransferWithSubLimits(String id,
			AnnexureTransferWithSubLimitsDTO.AnnexureTransferWithSubLimitsRequestDTO annexureTransferWithSubLimits) {
		GenericResponseDTO<Object> respose = new GenericResponseDTO<Object>();
		try {
			AnnexureTransferWithSubLimits annrexureTransferWithSubLimitsEntity = annexureTransferSubLimitsRepo
					.findById(id).get();
			if (annrexureTransferWithSubLimitsEntity == null) {
				respose.setData(new HashMap<>());
				respose.setStatus("Failure");
				respose.setMessage("Annexure Transfer With Sub Limits Not Found");
				return respose;
			}
			if (annexureTransferWithSubLimits.getAnnexureTransferLimitsId() != null
					|| annexureTransferWithSubLimits.getAnnexureTransferLimitsId() != "") {
				annrexureTransferWithSubLimitsEntity.setAnnexureTransferLimits(annexureTransferLimitsRepo
						.findById(annexureTransferWithSubLimits.getAnnexureTransferLimitsId()).get());
			}
			if (annexureTransferWithSubLimits.getSubTypeLimit() != null
					|| annexureTransferWithSubLimits.getSubTypeLimit() != "") {
				annrexureTransferWithSubLimitsEntity.setSubTypeLimit(annexureTransferWithSubLimits.getSubTypeLimit());
			}
			annrexureTransferWithSubLimitsEntity = annexureTransferSubLimitsRepo
					.save(annrexureTransferWithSubLimitsEntity);
			respose.setData(
					modelMapper.map(annrexureTransferWithSubLimitsEntity, AnnexureTransferWithSubLimitsDTO.class));
			respose.setStatus("Success");
			respose.setMessage("Annexure Transfer With Sub Limits Updated Successfully");
			return respose;
		} catch (Exception e) {
			logger.error("Error occur during execute updateAnnexureTransferWithSubLimits : {}", e.getMessage());
			respose.setData(new HashMap<>());
			respose.setStatus("Failure");
			respose.setMessage("Something went wrong.");
			return respose;
		}
	}

	@Override
	public GenericResponseDTO<Object> deleteAnnexureTransferWithSubLimits(String id) {
		GenericResponseDTO<Object> response = new GenericResponseDTO<Object>();
		try {
			annexureTransferSubLimitsRepo.deleteById(id);
			response.setData(new HashMap<>());
			response.setStatus("Success");
			response.setMessage("Annexure Transfer With Sub Limits Deleted Successfully");
			return response;
		} catch (Exception e) {
			logger.error("Error occur during execute deleteAnnexureTransferWithSubLimits : {}", e.getMessage());
			response.setData(new HashMap<>());
			response.setStatus("Failure");
			response.setMessage("Something went wrong.");
			return response;
		}
	}

}
