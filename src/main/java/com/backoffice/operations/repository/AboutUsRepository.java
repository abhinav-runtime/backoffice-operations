package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.AboutUs;

public interface AboutUsRepository extends JpaRepository<AboutUs, Long> {
    // You can add custom queries if needed
}
