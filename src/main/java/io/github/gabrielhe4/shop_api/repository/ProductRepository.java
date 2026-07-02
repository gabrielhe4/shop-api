package io.github.gabrielhe4.shop_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import io.github.gabrielhe4.shop_api.model.Category;
import io.github.gabrielhe4.shop_api.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategory(Category category, Pageable pageable);

    Page<Product> findByNameLikeIgnoreCase(String keyword, Pageable pageDetails);

}
