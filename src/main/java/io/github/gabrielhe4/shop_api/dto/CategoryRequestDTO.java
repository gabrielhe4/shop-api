package io.github.gabrielhe4.shop_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequestDTO(
    @NotNull(message = "Name of the category is required") 
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    String name,
    
    String description
) {

}
