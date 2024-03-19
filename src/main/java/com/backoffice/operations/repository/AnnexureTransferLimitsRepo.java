package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backoffice.operations.entity.AnnexureTransferLimits;

@Repository
public interface AnnexureTransferLimitsRepo extends JpaRepository<AnnexureTransferLimits, String> {

}
