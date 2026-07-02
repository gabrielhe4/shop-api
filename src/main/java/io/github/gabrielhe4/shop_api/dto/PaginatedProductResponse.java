package io.github.gabrielhe4.shop_api.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaginatedProductResponse {

    List<ProductDTO> content;
    Integer pageNumber;
    Integer pageSize;
    Long totalElements;
    Integer totalPages;
    Boolean lastPage;

}
