package io.github.gabrielhe4.shop_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.gabrielhe4.shop_api.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByNameIgnoreCase(String name);

}
