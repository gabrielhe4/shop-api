package io.github.gabrielhe4.shop_api.service.impl;

import org.springframework.stereotype.Service;

import io.github.gabrielhe4.shop_api.dto.CategoryDTO;
import io.github.gabrielhe4.shop_api.dto.CategoryRequestDTO;
import io.github.gabrielhe4.shop_api.exception.ResourceNotFoundException;
import io.github.gabrielhe4.shop_api.mapper.CategoryMapper;
import io.github.gabrielhe4.shop_api.model.Category;
import io.github.gabrielhe4.shop_api.repository.CategoryRepository;
import io.github.gabrielhe4.shop_api.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void createCategory(CategoryRequestDTO request) {
        Category newCategory = CategoryMapper.INSTANCE.toNewEntity(request);
        categoryRepository.save(newCategory);
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryRequestDTO request) {
        
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        CategoryMapper.INSTANCE.updateEntity(request, existingCategory);
        categoryRepository.save(existingCategory);

        return CategoryMapper.INSTANCE.toDTO(existingCategory);
    }

}
