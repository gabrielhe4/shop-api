package io.github.gabrielhe4.shop_api.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "SignupRequest",
    description = "User registration request containing username, email, password and roles",
    example = """
        {
          "username": "john_doe",
          "email": "john@example.com",
          "roles": ["USER"],
          "password": "SecurePass123!"
        }
        """
)
public record SignupRequest(

    @Size(min = 3, max = 20)
    @Schema(description = "Unique username for the account (3-20 characters)", example = "john_doe", requiredMode = Schema.RequiredMode.REQUIRED)
    String username,

    @NotBlank
    @Size(max = 50)
    @Email
    @Schema(description = "Valid email address (max 50 characters)", example = "user@example.com")
    String email,

    @Schema(
        description = "List of roles/permissions for the user (JSON array format)",
        implementation = Set.class,
        example = "[\"USER\", \"ADMIN\"]"
    )
    Set<String> roles,

    @NotBlank
    @Size(min = 12, max = 40)
    @Schema(description = "Password (12-40 characters, must meet complexity requirements)", example = "SecurePass123!")
    String password
) {

}
