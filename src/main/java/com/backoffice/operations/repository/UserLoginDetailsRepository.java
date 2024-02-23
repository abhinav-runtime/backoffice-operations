package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backoffice.operations.entity.UserLoginDetails;

public interface UserLoginDetailsRepository extends JpaRepository <UserLoginDetails, String>{

}
