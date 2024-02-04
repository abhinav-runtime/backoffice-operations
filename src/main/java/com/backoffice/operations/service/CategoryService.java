package com.backoffice.operations.service;


import java.util.List;

import com.backoffice.operations.payloads.CategoryDto;

public interface CategoryService {
    CategoryDto addCategory(CategoryDto categoryDto);

    CategoryDto getCategory(String categoryId);

    List<CategoryDto> getAllCategories();

    CategoryDto updateCategory(CategoryDto categoryDto, String categoryId);

    void deleteCategory(String categoryId);
}
