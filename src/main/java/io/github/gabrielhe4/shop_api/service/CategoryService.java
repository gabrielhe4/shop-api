package io.github.gabrielhe4.shop_api.service;

import io.github.gabrielhe4.shop_api.dto.CategoryDTO;
import io.github.gabrielhe4.shop_api.dto.CategoryRequestDTO;

public interface CategoryService {

    void createCategory(CategoryRequestDTO request);

    CategoryDTO updateCategory(Long id, CategoryRequestDTO request);
    
}
