package io.github.gabrielhe4.shop_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.gabrielhe4.shop_api.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
