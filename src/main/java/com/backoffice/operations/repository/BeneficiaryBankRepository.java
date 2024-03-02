package com.backoffice.operations.repository;

import com.backoffice.operations.entity.BeneficiaryBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeneficiaryBankRepository extends JpaRepository<BeneficiaryBank, String> {
}
