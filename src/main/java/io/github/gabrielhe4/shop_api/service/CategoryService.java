package io.github.gabrielhe4.shop_api.service;

import io.github.gabrielhe4.shop_api.dto.CategoryDTO;
import io.github.gabrielhe4.shop_api.dto.CategoryRequestDTO;
import io.github.gabrielhe4.shop_api.dto.PaginatedCategoryResponse;

public interface CategoryService {

    void createCategory(CategoryRequestDTO request);

    CategoryDTO updateCategory(Long id, CategoryRequestDTO request);

    PaginatedCategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    void deleteCategory(Long id);
    
}
