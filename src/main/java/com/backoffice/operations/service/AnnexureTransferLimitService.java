package com.backoffice.operations.service;

import com.backoffice.operations.payloads.AnnexureTransferLimitsDTO;
import com.backoffice.operations.payloads.AnnexureTransferWithSubLimitsDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;

public interface AnnexureTransferLimitService {
	GenericResponseDTO<Object> getAllAnnexureTransferLimits();

	GenericResponseDTO<Object> getAnnexureTransferLimitsById(String id);

	GenericResponseDTO<Object> createAnnexureTransferLimits(AnnexureTransferLimitsDTO annexureTransferLimits);

	GenericResponseDTO<Object> updateAnnexureTransferLimits(String id,
			AnnexureTransferLimitsDTO annexureTransferLimits);

	GenericResponseDTO<Object> deleteAnnexureTransferLimits(String id);

	GenericResponseDTO<Object> getAllAnnexureTransferWithSubLimits();

	GenericResponseDTO<Object> getAnnexureTransferWithSubLimitsById(String id);

	GenericResponseDTO<Object> createAnnexureTransferWithSubLimits(
			AnnexureTransferWithSubLimitsDTO.AnnexureTransferWithSubLimitsRequestDTO annexureTransferWithSubLimits);

	GenericResponseDTO<Object> updateAnnexureTransferWithSubLimits(String id,
			AnnexureTransferWithSubLimitsDTO.AnnexureTransferWithSubLimitsRequestDTO annexureTransferWithSubLimits);

	GenericResponseDTO<Object> deleteAnnexureTransferWithSubLimits(String id);
}
