package io.github.gabrielhe4.shop_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.gabrielhe4.shop_api.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
