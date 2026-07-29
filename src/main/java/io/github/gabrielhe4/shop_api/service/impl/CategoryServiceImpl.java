package io.github.gabrielhe4.shop_api.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.github.gabrielhe4.shop_api.dto.CategoryDTO;
import io.github.gabrielhe4.shop_api.dto.CategoryRequest;
import io.github.gabrielhe4.shop_api.dto.PaginatedCategoryResponse;
import io.github.gabrielhe4.shop_api.exception.ResourceAlreadyExistsException;
import io.github.gabrielhe4.shop_api.exception.ResourceNotFoundException;
import io.github.gabrielhe4.shop_api.mapper.CategoryMapper;
import io.github.gabrielhe4.shop_api.model.Category;
import io.github.gabrielhe4.shop_api.repository.CategoryRepository;
import io.github.gabrielhe4.shop_api.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final static Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void createCategory(CategoryRequest request) {

        log.info("Creating new category...");

        if (categoryRepository.existsByNameIgnoreCase(request.name()))
            throw new ResourceAlreadyExistsException("Category", request.name());

        Category newCategory = CategoryMapper.INSTANCE.toNewEntity(request);

        categoryRepository.save(newCategory);
        log.info("New category created with ID: {}", newCategory.getId());
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryRequest request) {

        log.info("Updating category with ID: {}", id);

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        CategoryMapper.INSTANCE.updateEntity(request, existingCategory);
        categoryRepository.save(existingCategory);

        log.info("Category with ID {} was updated succesfully.", id);

        return CategoryMapper.INSTANCE.toDTO(existingCategory);
    }

    @Override
    public PaginatedCategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder) {

        log.info("Obtaining all categories...");

        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

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

        log.info("Returning paginated categories successfully.");

        return response;
    }

    @Override
    public void deleteCategory(Long id) {

        log.info("Deleting category with ID: {}");

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        categoryRepository.delete(existingCategory);

        log.info("Category with ID: {} was deleted successfully.", id);
    }

}
