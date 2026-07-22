package io.github.gabrielhe4.shop_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Schema(name = "ProductDTO", description = "Product Data Transfer Object")
public class ProductDTO {

    @Schema(description = "Product ID")
    Long id;

    @Schema(description = "Product name")
    String name;

    @Schema(description = "Product description")
    String description;

    @Schema(description = "Product image URL")
    String image;

    @Setter
    @Schema(description = "Current stock quantity")
    Integer stock;

    @Schema(description = "Regular price")
    Double price;

    @Schema(description = "Discount percentage")
    Double discount;

    @Schema(description = "Special/promotional price")
    Double specialPrice;
    
}
