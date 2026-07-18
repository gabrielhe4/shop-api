package io.github.gabrielhe4.shop_api.dto;

import java.util.ArrayList;
import java.util.List;

public record CartResponse (
    Long id,
    Double totalPrice,
    List<ProductDTO> products
    
) {
    public CartResponse(Long id, List<ProductDTO> products) {
        this(id, 0.0, new ArrayList<>());
    }

}
