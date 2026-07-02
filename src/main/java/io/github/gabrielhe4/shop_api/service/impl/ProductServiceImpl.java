package io.github.gabrielhe4.shop_api.service.impl;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.github.gabrielhe4.shop_api.dto.PaginatedProductResponse;
import io.github.gabrielhe4.shop_api.dto.ProductDTO;
import io.github.gabrielhe4.shop_api.dto.ProductRequest;
import io.github.gabrielhe4.shop_api.exception.ResourceNotFoundException;
import io.github.gabrielhe4.shop_api.mapper.ProductMapper;
import io.github.gabrielhe4.shop_api.model.Category;
import io.github.gabrielhe4.shop_api.model.Product;
import io.github.gabrielhe4.shop_api.repository.CategoryRepository;
import io.github.gabrielhe4.shop_api.repository.ProductRepository;
import io.github.gabrielhe4.shop_api.service.FileService;
import io.github.gabrielhe4.shop_api.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    private final FileService fileService;

    @Value("${project.image}")
    private String path;

    public ProductServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository, 
            FileService fileService) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.fileService = fileService;
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
        
        Product existingProduct = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDiscount(product.getDiscount());
        existingProduct.setSpecialPrice(specialPrice);

        productRepository.save(existingProduct);

        return ProductMapper.INSTANCE.toDTO(existingProduct);

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

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product existingProduct = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        String fileName = fileService.uploadImage(path, image);
        existingProduct.setImage(fileName);

        Product updatedProduct = productRepository.save(existingProduct);
        return ProductMapper.INSTANCE.toDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long productId) {
        
        Product existingProduct = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // TODO: Delete image from path

        productRepository.delete(existingProduct);
    }

}
