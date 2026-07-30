package io.github.gabrielhe4.shop_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "PaginatedProductResponse", description = "Paginated response containing a list of products and pagination metadata")
public class PaginatedProductResponse {

    @Schema(description = "List of product items in the page", requiredMode = Schema.RequiredMode.REQUIRED)
    List<ProductDTO> content;

    @Schema(description = "Current page number (0-based index)", requiredMode = Schema.RequiredMode.REQUIRED)
    Integer pageNumber;

    @Schema(description = "Number of elements per page", requiredMode = Schema.RequiredMode.REQUIRED)
    Integer pageSize;

    @Schema(description = "Total number of elements across all pages", requiredMode = Schema.RequiredMode.REQUIRED)
    Long totalElements;

    @Schema(description = "Total number of pages", requiredMode = Schema.RequiredMode.REQUIRED)
    Integer totalPages;

    @Schema(description = "True if this is the last page")
    Boolean lastPage;

}
