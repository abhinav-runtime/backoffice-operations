package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.SecuritySettings;

public interface SecuritySettingsRepository extends JpaRepository<SecuritySettings, Long> {

}
