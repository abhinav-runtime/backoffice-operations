package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.ContactUSEntity;

public interface ContactUSRepository extends JpaRepository<ContactUSEntity, String> {
}
