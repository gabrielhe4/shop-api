package io.github.gabrielhe4.shop_api.dto;

import java.util.ArrayList;
import java.util.List;

public record CartResponse (
    Long id,
    Double totalPrice,
    List<ItemResponse> items
    
) {
    public CartResponse(Long id, List<ItemResponse> items) {
        this(id, 0.0, new ArrayList<>());
    }

}
