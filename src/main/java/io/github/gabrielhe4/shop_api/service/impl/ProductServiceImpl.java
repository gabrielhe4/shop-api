package io.github.gabrielhe4.shop_api.service.impl;


import org.springframework.stereotype.Service;

import io.github.gabrielhe4.shop_api.dto.PaginatedProductResponse;
import io.github.gabrielhe4.shop_api.dto.ProductDTO;
import io.github.gabrielhe4.shop_api.dto.ProductRequest;
import io.github.gabrielhe4.shop_api.exception.ResourceNotFoundException;
import io.github.gabrielhe4.shop_api.mapper.ProductMapper;
import io.github.gabrielhe4.shop_api.model.Category;
import io.github.gabrielhe4.shop_api.model.Product;
import io.github.gabrielhe4.shop_api.repository.CategoryRepository;
import io.github.gabrielhe4.shop_api.repository.ProductRepository;
import io.github.gabrielhe4.shop_api.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    public ProductServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public ProductDTO addProduct(Long categoryId, ProductRequest request) {
       
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        double specialPrice = request.price() - ((request.discount() * 0.01) * request.price());
        
        Product product = ProductMapper.INSTANCE.toEntity(request);

        product.setImage("default-image.png");
        product.setSpecialPrice(specialPrice);
        product.setCategory(category);

        productRepository.save(product);

        return ProductMapper.INSTANCE.toDTO(product);
    }

    @Override
    public PaginatedProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllProducts'");
    }

    @Override
    public PaginatedProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize,
            String sortBy, String sortOrder) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchByCategory'");
    }

    @Override
    public PaginatedProductResponse searchProductBy(String keyword, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchProductBy'");
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO product) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProduct'");
    }

}
