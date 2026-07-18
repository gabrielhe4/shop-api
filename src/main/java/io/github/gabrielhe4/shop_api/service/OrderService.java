package io.github.gabrielhe4.shop_api.service;

import io.github.gabrielhe4.shop_api.dto.OrderDTO;
import io.github.gabrielhe4.shop_api.dto.OrderRequest;

public interface OrderService {

    OrderDTO placeOrder(String email, String paymentMethod, OrderRequest request);

}
