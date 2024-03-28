package com.backoffice.operations.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backoffice.operations.entity.AnnexureTransferLimits;
import com.backoffice.operations.entity.AnnexureTransferWithSubLimits;

@Repository
public interface AnnexureTransferSubLimitsRepo extends JpaRepository<AnnexureTransferWithSubLimits, String> {
	AnnexureTransferWithSubLimits findByAnnexureTransferLimitsAndSubTypeLimit(AnnexureTransferLimits annexureTransferLimits, String subType);
	List<AnnexureTransferWithSubLimits> findByAnnexureTransferLimits(AnnexureTransferLimits annexureTransferLimits);
}
