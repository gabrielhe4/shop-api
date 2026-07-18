package io.github.gabrielhe4.shop_api.service;

import io.github.gabrielhe4.shop_api.dto.CategoryDTO;
import io.github.gabrielhe4.shop_api.dto.CategoryRequest;
import io.github.gabrielhe4.shop_api.dto.PaginatedCategoryResponse;

public interface CategoryService {

    void createCategory(CategoryRequest request);

    CategoryDTO updateCategory(Long id, CategoryRequest request);

    PaginatedCategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    void deleteCategory(Long id);
    
}
