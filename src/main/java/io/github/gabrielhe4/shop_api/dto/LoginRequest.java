package io.github.gabrielhe4.shop_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
    name = "LoginRequest",
    description = "Request body for user login authentication",
    example = """
        {
            "username": "randomuser133",
            "password": "SecurePassword123!"
        }"""
)
public record LoginRequest(
    @Schema(
        name = "username",
        description = "The username or email of the user attempting to log in",
        example = "randomuser133"
    ) 
    @NotBlank(message = "Username is required")
    String username, 
    
    @Schema(
        name = "password",
        description = "The user's password for authentication",
        example = "SecurePassword123!"
    ) 
    @NotBlank(message = "Password is required")
    String password
) {

}
