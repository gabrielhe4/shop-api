package io.github.gabrielhe4.shop_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.gabrielhe4.shop_api.dto.CategoryDTO;
import io.github.gabrielhe4.shop_api.dto.CategoryRequestDTO;
import io.github.gabrielhe4.shop_api.service.CategoryService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/admin/category")
    public ResponseEntity<Void> createCategory(@Valid @RequestBody CategoryRequestDTO request) {
        
        categoryService.createCategory(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequestDTO request) {
        
        CategoryDTO response = categoryService.updateCategory(id, request);
        
        return ResponseEntity.ok(response);
    }
    
}
