package io.github.gabrielhe4.shop_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.gabrielhe4.shop_api.config.AppConstants;
import io.github.gabrielhe4.shop_api.dto.CategoryDTO;
import io.github.gabrielhe4.shop_api.dto.CategoryRequest;
import io.github.gabrielhe4.shop_api.dto.PaginatedCategoryResponse;
import io.github.gabrielhe4.shop_api.service.CategoryService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/admin/category")
    public ResponseEntity<Void> createCategory(@Valid @RequestBody CategoryRequest request) {
        
        categoryService.createCategory(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/admin/categories/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        
        CategoryDTO response = categoryService.updateCategory(id, request);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/categories")
    public ResponseEntity<PaginatedCategoryResponse> getAllCategories(
        @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
        @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
        @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CUSTOMERS_BY) String sortBy,
        @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder
    ) {
        
        var response = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {

        categoryService.deleteCategory(id);

        return ResponseEntity.noContent().build();
        
    }

}
