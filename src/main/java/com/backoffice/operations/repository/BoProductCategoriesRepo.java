package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backoffice.operations.entity.BoProductCategories;

@Repository
public interface BoProductCategoriesRepo extends JpaRepository<BoProductCategories, String> {
	BoProductCategories findByCategoriesName(String categoriesName);
}
