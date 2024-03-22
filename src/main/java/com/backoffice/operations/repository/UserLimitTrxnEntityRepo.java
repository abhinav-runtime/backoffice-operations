package com.backoffice.operations.repository;

import com.backoffice.operations.entity.AnnexureTransferWithSubLimits;
import com.backoffice.operations.entity.UserLimitTrxnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLimitTrxnEntityRepo extends JpaRepository<UserLimitTrxnEntity, String> {

    UserLimitTrxnEntity findByUniqueKey(String uniqueKey);
}
