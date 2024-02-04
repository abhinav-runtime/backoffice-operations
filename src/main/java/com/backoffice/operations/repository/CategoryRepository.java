package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
