package io.github.gabrielhe4.shop_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AddressRequest {

    @NotBlank(message = "Street cannot be blank")
    @Size(max = 45, message = "Street must be less than or equal to 45 characters")
    private String street;

    @NotBlank(message = "Building Name cannot be blank")
    @Size(max = 45, message = "Building Name must be less than or equal to 45 characters")
    private String buildingName;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 45, message = "City must be less than or equal to 45 characters")
    private String city;

    @NotBlank(message = "State cannot be blank")
    @Size(max = 45, message = "State must be less than or equal to 45 characters")
    private String state;

    @NotBlank(message = "Country cannot be blank")
    @Size(max = 45, message = "Country must be less than or equal to 45 characters")
    private String country;

    @NotBlank(message = "Zipcode cannot be blank")
    @Size(min = 5, max = 10, message = "Zipcode must be between 5 and 10 characters")
    private String zipcode;

}
