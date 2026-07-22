package io.github.gabrielhe4.shop_api.dto;

import io.github.gabrielhe4.shop_api.model.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "AddressRequest",
    implementation = AddressRequest.class,
    description = "Address information for an address request",
    example = """
        {
            "street": "123 Main St",
            "buildingName": "Apartment Block A",
            "city": "New York",
            "state": "NY",
            "country": "United States",
            "zipcode": "10001"
        }
    """
)
public record AddressRequest (

        @NotBlank(message = "Street cannot be blank")
        @Size(max = 45, message = "Street must be less than or equal to 45 characters")
        String street,

        @NotBlank(message = "Building Name cannot be blank")
        @Size(max = 45, message = "Building Name must be less than or equal to 45 characters")
        String buildingName,

        @NotBlank(message = "City cannot be blank")
        @Size(max = 45, message = "City must be less than or equal to 45 characters")
        String city,

        @NotBlank(message = "State cannot be blank")
        @Size(max = 45, message = "State must be less than or equal to 45 characters")
        String state,

        @NotBlank(message = "Country cannot be blank")
        @Size(max = 45, message = "Country must be less than or equal to 45 characters")
        String country,

        @NotBlank(message = "Zipcode cannot be blank")
        @Size(min = 5, max = 10, message = "Zipcode must be between 5 and 10 characters")
        String zipcode
    ) {

        public static Address toAddress(AddressRequest request) { 
            return Address.builder()
                .street(request.street())
                .buildingName(request.buildingName())
                .city(request.city())
                .state(request.state())
                .country(request.country())
                .zipcode(request.zipcode())
                .build();
        }
}

