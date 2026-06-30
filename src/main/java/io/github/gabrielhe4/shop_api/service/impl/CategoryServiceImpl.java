package io.github.gabrielhe4.shop_api.service.impl;

import org.springframework.stereotype.Service;

import io.github.gabrielhe4.shop_api.dto.CategoryRequestDTO;
import io.github.gabrielhe4.shop_api.mapper.CategoryMapper;
import io.github.gabrielhe4.shop_api.model.Category;
import io.github.gabrielhe4.shop_api.repository.CategoryRepository;
import io.github.gabrielhe4.shop_api.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryMapper categoryMapper, CategoryRepository categoryRepository) {
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void createCategory(CategoryRequestDTO request) {
        Category newCategory = categoryMapper.toNewEntity(request);
        categoryRepository.save(newCategory);
    }

}
