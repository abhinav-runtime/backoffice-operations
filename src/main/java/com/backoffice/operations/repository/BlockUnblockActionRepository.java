package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backoffice.operations.entity.BlockUnblockAction;

public interface BlockUnblockActionRepository extends JpaRepository<BlockUnblockAction, String> {

}
