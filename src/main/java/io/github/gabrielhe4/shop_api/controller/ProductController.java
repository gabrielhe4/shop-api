package io.github.gabrielhe4.shop_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.github.gabrielhe4.shop_api.config.AppConstants;
import io.github.gabrielhe4.shop_api.dto.PaginatedProductResponse;
import io.github.gabrielhe4.shop_api.dto.ProductDTO;
import io.github.gabrielhe4.shop_api.dto.ProductRequest;
import io.github.gabrielhe4.shop_api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.io.IOException;

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
@Tag(name = "Products", description = "Product management endpoints")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/admin/categories/{id}/product")
    @Operation(summary = "Create a new product", description = "Creates a new product under the specified category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<ProductDTO> createProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        ProductDTO response = productService.addProduct(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/public/products")
    @Operation(summary = "Get all products", description = "Retrieves a paginated list of all products sorted by customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<PaginatedProductResponse> findAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CUSTOMERS_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder) {

        var response = productService.getAllProducts(
                pageNumber,
                pageSize,
                sortBy,
                sortOrder);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/categories/{id}/products")
    @Operation(summary = "Get products by category", description = "Retrieves a paginated list of products for the specified category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<PaginatedProductResponse> getProductsByCategory(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CUSTOMERS_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder,
            @PathVariable Long id) {
        var response = productService.searchByCategory(
                id,
                pageNumber,
                pageSize,
                sortBy,
                sortOrder);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    @Operation(summary = "Search products by keyword", description = "Retrieves a paginated list of products matching the given keyword")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Keyword not found")
    })
    public ResponseEntity<PaginatedProductResponse> getProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CUSTOMERS_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder,
            @PathVariable Long id) {
        var response = productService.searchProductByKeyword(
                keyword,
                pageNumber,
                pageSize,
                sortBy,
                sortOrder);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/products/{productId}/image")
    @Operation(summary = "Update product image", description = "Updates the image for the specified product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid image file")
    })
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
            @RequestParam("image") MultipartFile image) throws IOException {
        ProductDTO updatedProduct = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    @Operation(summary = "Delete a product", description = "Deletes the specified product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {

        productService.deleteProduct(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
