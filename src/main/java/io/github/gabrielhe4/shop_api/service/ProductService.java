package io.github.gabrielhe4.shop_api.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import io.github.gabrielhe4.shop_api.dto.PaginatedProductResponse;
import io.github.gabrielhe4.shop_api.dto.ProductDTO;
import io.github.gabrielhe4.shop_api.dto.ProductRequest;

public interface ProductService {

    ProductDTO addProduct(Long categoryId, ProductRequest request);

    PaginatedProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    PaginatedProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    PaginatedProductResponse searchProductBy(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO updateProduct(Long productId, ProductDTO product);

    ProductDTO updateProductImage (Long productId, MultipartFile image) throws IOException;

    void deleteProduct(Long productId);

}
