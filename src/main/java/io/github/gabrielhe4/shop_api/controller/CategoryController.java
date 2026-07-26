package io.github.gabrielhe4.shop_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.gabrielhe4.shop_api.config.AppConstants;
import io.github.gabrielhe4.shop_api.dto.CategoryDTO;
import io.github.gabrielhe4.shop_api.dto.CategoryRequest;
import io.github.gabrielhe4.shop_api.dto.PaginatedCategoryResponse;
import io.github.gabrielhe4.shop_api.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
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

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;

@RestController
@RequestMapping("/api")
@Tag(name = "Categories", description = "Category management endpoints")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/admin/category")
    @Operation(summary = "Create a new category", description = "Creates a new category in the system. Only accessible via admin endpoint.", tags = {
            "Categories" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or missing required fields")
    })
    public ResponseEntity<Void> createCategory(@Valid @RequestBody CategoryRequest request) {

        categoryService.createCategory(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/admin/categories/{id}")
    @Operation(summary = "Update an existing category", description = "Updates the information for a specific category by its ID.", tags = {
            "Categories" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body or missing required fields"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {

        CategoryDTO response = categoryService.updateCategory(id, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/categories")
    @Operation(summary = "Get all categories with pagination", description = "Retrieves a paginated list of all categories. Supports sorting and filtering.", tags = {
            "Categories" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of categories"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<PaginatedCategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CUSTOMERS_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder) {

        var response = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/categories/{id}")
    @Operation(summary = "Delete a category", description = "Deletes a category by its ID. Only accessible via admin endpoint.", tags = {
            "Categories" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {

        categoryService.deleteCategory(id);

        return ResponseEntity.noContent().build();

    }

}
