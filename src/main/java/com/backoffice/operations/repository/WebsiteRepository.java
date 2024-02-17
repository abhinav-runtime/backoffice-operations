package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.WebsiteEntity;

public interface WebsiteRepository extends JpaRepository<WebsiteEntity, String> {
}