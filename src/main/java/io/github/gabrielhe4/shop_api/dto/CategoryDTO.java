package io.github.gabrielhe4.shop_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Category", description = "Represents a category in the shop")
public record CategoryDTO(
    @Schema(description = "Unique identifier of the category", example = "1")
    Long id,
    
    @Schema(description = "Name of the category", example = "Electronics")
    String name,
    
    @Schema(description = "Description of the category", example = "All electronic devices and accessories")
    String description
) {

}
