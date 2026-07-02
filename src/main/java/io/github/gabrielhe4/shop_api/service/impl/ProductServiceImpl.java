package io.github.gabrielhe4.shop_api.service.impl;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) 
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findAll(pageable);

        var response = buildPaginatedResponse(productPage);

        return response;
            
    }

    @Override
    public PaginatedProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize,
            String sortBy, String sortOrder) {
        
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) 
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findByCategory(category, pageable);

        var response = buildPaginatedResponse(productPage);

        return response;
    }

    @Override
    public PaginatedProductResponse searchProductBy(String keyword, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) 
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findByNameLikeIgnoreCase(keyword, pageDetails);

        var response = buildPaginatedResponse(productPage);

        return response;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO product) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProduct'");
    }

    private PaginatedProductResponse buildPaginatedResponse(Page<Product> productPage) {
        List<ProductDTO> productDTOs = productPage.getContent()
            .stream()
            .map(ProductMapper.INSTANCE::toDTO)
            .toList();

        return PaginatedProductResponse.builder()
            .content(productDTOs)
            .pageNumber(productPage.getNumber())
            .pageSize(productPage.getSize())
            .totalElements(productPage.getTotalElements())
            .totalPages(productPage.getTotalPages())
            .lastPage(productPage.isLast())
            .build();
    }

}
