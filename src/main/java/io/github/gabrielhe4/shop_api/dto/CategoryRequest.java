package io.github.gabrielhe4.shop_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "CategoryRequest", description = "Request body for creating a new category", 
example = """
{
  "name": "Electronics",
  "description": "Electronic devices and gadgets"
}""")
public record CategoryRequest(

    @Schema(description = "Name of the category (required)", pattern = "^.{3,20}$", minLength = 3, maxLength = 20) 
    @NotNull(message = "Name of the category is required") 
    @NotBlank(message = "Name of the category cannot be blank")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    String name,
    
    @Schema(description = "Description of the category (optional)")
    String description
) {

}
