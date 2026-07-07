package io.github.gabrielhe4.shop_api.service;

import java.util.List;

import io.github.gabrielhe4.shop_api.dto.CartDTO;
import jakarta.transaction.Transactional;

public interface CartService {

    CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCart();

    @Transactional
    CartDTO updateProductQuantityInCart(Long productId, Integer quantity);

    String deleteProductFromCart(Long cartId, Long productId);

    void updateProductInCarts(Long cartId, Long productId);

}
