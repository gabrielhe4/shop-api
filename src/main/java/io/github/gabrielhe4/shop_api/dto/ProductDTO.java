package io.github.gabrielhe4.shop_api.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductDTO {

    Long id;
    String name;
    String description;
    String image;
    Integer quantity;
    Double price;
    Double discount;
    Double specialPrice;
    
}
