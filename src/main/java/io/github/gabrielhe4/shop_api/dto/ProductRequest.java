package io.github.gabrielhe4.shop_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductRequest(

    @NotNull(message = "Product name is required")
    @Size(min = 3, max = 20, message = "Product name must be between 3 and 20 characters")
    String name,

    @NotNull(message = "Product description is required")
    @Size(min = 10, max = 200, message = "Product description must be between 10 and 200 characters")
    String description,
    
    @NotNull(message = "Stock is required")
    Integer stock,

    @NotNull(message = "Price is required")
    Double price,

    @NotNull(message = "Discount is required")
    Double discount
) {

}
