package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backoffice.operations.entity.AnnexureTransferWithSubLimits;

@Repository
public interface AnnexureTransferSubLimitsRepo extends JpaRepository<AnnexureTransferWithSubLimits, String> {

}
