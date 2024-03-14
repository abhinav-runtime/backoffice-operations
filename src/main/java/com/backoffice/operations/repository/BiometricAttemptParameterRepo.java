package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.BiometricAttemptParameter;

public interface BiometricAttemptParameterRepo extends JpaRepository<BiometricAttemptParameter, Long> {
}
