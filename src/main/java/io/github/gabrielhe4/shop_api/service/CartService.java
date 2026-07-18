package io.github.gabrielhe4.shop_api.service;

import java.util.List;

import io.github.gabrielhe4.shop_api.dto.CartResponse;

public interface CartService {

    CartResponse addProductToCart(Long productId, Integer quantity);

    List<CartResponse> getAllCarts();

    CartResponse getCart();

    CartResponse updateProductQuantityInCart(Long productId, Integer quantity);

    String deleteProductFromCart(Long productId);

    void updateProductInCarts(Long cartId, Long productId);

}
