package io.github.gabrielhe4.shop_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * Request DTO for creating or updating a product.
 * <p>
 * Contains validation constraints and Swagger schema definitions with examples.
 */
@Schema(
    name = "ProductRequest",
    implementation = ProductRequest.class,
    description = "DTO for creating a new product in the shop API",
    example = """
        {
            "name": "Wireless Bluetooth Headphones",
            "description": "High-quality wireless headphones with noise cancellation and 30-hour battery life. Features premium sound quality and comfortable over-ear design.",
            "stock": 50,
            "price": 129.99,
            "discount": 15.0
        }
        """
)
public record ProductRequest(

    @NotNull(message = "Product name is required")
    @Size(min = 3, max = 20, message = "Product name must be between 3 and 20 characters")
    @Schema(
        name = "name",
        example = "Wireless Bluetooth Headphones",
        defaultValue = "Wireless Bluetooth Headphones"
    )
    String name,

    @NotNull(message = "Product description is required")
    @Size(min = 10, max = 200, message = "Product description must be between 10 and 200 characters")
    @Schema(
        name = "description",
        example = "High-quality wireless headphones with noise cancellation and 30-hour battery life. Features premium sound quality and comfortable over-ear design.",
        defaultValue = "High-quality wireless headphones with noise cancellation and 30-hour battery life"
    )
    String description, 

    @NotNull(message = "Stock is required")
    @Schema(
        name = "stock",
        example = "50",
        implementation = Integer.class,
        defaultValue = "50"
    )
    Integer stock,
    @NotNull(message = "Price is required")
    @Schema(
        name = "price",
        example = "129.99",
        implementation = Double.class,
        defaultValue = "129.99"
    )
    Double price,
    
    @NotNull(message = "Discount is required")
    @Schema(
        name = "discount",
        example = "15.0",
        implementation = Double.class,
        defaultValue = "15.0"
    )
    Double discount
) {

}
