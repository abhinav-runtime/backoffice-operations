package com.backoffice.operations.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backoffice.operations.entity.BoProductCategories;
import com.backoffice.operations.entity.BoProductSubCategories;

@Repository
public interface BoProductSubCategoriesRepo extends JpaRepository<BoProductSubCategories, String> {
	BoProductSubCategories findBySubCategoriesName(String subCategoriesName);
	List<BoProductSubCategories> findByCategories(BoProductCategories category);
}
