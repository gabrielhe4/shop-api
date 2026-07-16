package io.github.gabrielhe4.shop_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartRequest(
    @NotNull @Positive(message = "Product ID must be over 0") Long productId,
    @NotNull @Positive(message = "Quantity must be over 0") Integer quantity
) {

}
