package io.github.gabrielhe4.shop_api.service;

import io.github.gabrielhe4.shop_api.dto.OrderResponse;
import io.github.gabrielhe4.shop_api.dto.OrderRequest;

public interface OrderService {

    OrderResponse placeOrder(String email, String paymentMethod, OrderRequest request);

}
