package io.github.gabrielhe4.shop_api.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.github.gabrielhe4.shop_api.dto.CategoryDTO;
import io.github.gabrielhe4.shop_api.dto.CategoryRequestDTO;
import io.github.gabrielhe4.shop_api.dto.PaginatedCategoryResponse;
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

    @Override
    public PaginatedCategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? 
            Sort.by(sortBy).ascending() : 
            Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<CategoryDTO> categoryDTOs = categoryPage.getContent()
            .stream()
            .map(CategoryMapper.INSTANCE::toDTO)
            .toList();

        var response = PaginatedCategoryResponse.builder()
            .categories(categoryDTOs)
            .pageNumber(categoryPage.getNumber())
            .pageSize(categoryPage.getSize())
            .totalElements(categoryPage.getTotalElements())
            .totalPages(categoryPage.getTotalPages())
            .lastPage(categoryPage.isLast())
            .build();
        
        return response;
    }

    @Override
    public void deleteCategory(Long id) { 

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        categoryRepository.delete(existingCategory);
    }

}
