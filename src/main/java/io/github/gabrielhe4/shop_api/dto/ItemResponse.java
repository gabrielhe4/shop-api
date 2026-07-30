package io.github.gabrielhe4.shop_api.dto;

import io.github.gabrielhe4.shop_api.model.CartItem;
import io.github.gabrielhe4.shop_api.model.Product;

public record ItemResponse (

    Long id,
    String name,
    String image,
    Integer quantity,
    Double price,
    Double discount,
    Double specialPrice,
    Long productId

){
    public static ItemResponse from(CartItem cartItem) {
        Product product = cartItem.getProduct();
        return new ItemResponse(cartItem.getId(), 
            product.getName(), 
            product.getImage(),
            cartItem.getQuantity(), 
            product.getPrice(), 
            product.getDiscount(), 
            product.getSpecialPrice(), 
            product.getId());
    }
}
