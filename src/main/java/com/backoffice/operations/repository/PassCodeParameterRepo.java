package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backoffice.operations.entity.PasscodeParameter;

@Repository
public interface PassCodeParameterRepo extends JpaRepository<PasscodeParameter, Long> {

}
