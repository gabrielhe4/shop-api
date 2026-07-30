package io.github.gabrielhe4.shop_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(name = "CartRequest", description = "Request body for adding items to cart")
public record CartRequest(
    @NotNull @Positive(message = "Product ID must be over 0") Long productId,
    @NotNull @Positive(message = "Quantity must be over 0") Integer quantity
) {

}
