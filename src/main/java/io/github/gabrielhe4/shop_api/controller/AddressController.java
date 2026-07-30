package io.github.gabrielhe4.shop_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.gabrielhe4.shop_api.util.AuthUtil;
import jakarta.validation.Valid;
import io.github.gabrielhe4.shop_api.dto.AddressResponse;
import io.github.gabrielhe4.shop_api.dto.AddressRequest;
import io.github.gabrielhe4.shop_api.dto.MessageResponse;
import io.github.gabrielhe4.shop_api.model.User;
import io.github.gabrielhe4.shop_api.service.AddressService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Addresses", description = "Address management operations for users")
public class AddressController {

    private final AuthUtil authUtil;
    private final AddressService addressService;

    public AddressController(AuthUtil authUtil, AddressService addressService) {
        this.authUtil = authUtil;
        this.addressService = addressService;
    }

    @PostMapping("/user/addresses")
    @Operation(
        summary = "Create a new address",
        description = "Creates a new shipping address for the authenticated user"
    )
    @ApiResponse(
        responseCode = "201",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = AddressResponse.class)
        )
    )
    public ResponseEntity<AddressResponse> createdAddress(@Valid @RequestBody AddressRequest request) {

        User user = authUtil.getLoggedInUser();
        var response = addressService.create(request, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping("/admin/addresses")
    @Operation(
        summary = "Get all addresses",
        description = "Returns a list of all addresses in the system"
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = List.class)
        )
    )
    public ResponseEntity<List<AddressResponse>> getAddresses() {

        var response = addressService.getAllAddresses();
        return ResponseEntity.ok(response);

    }

    @GetMapping("/user/addresses/{addressId}")
    @Operation(
        summary = "Get address by ID",
        description = "Retrieves a specific address by its ID"
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = AddressResponse.class)
        )
    )
    public ResponseEntity<AddressResponse> getAddressById(@Parameter(description = "Address ID") @PathVariable Long addressId) {

        var response = addressService.getAddressById(addressId);
        return ResponseEntity.ok(response);

    }

    @GetMapping("user/addresses")
    @Operation(
        summary = "Get user's addresses",
        description = "Returns all addresses for the authenticated user"
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = List.class)
        )
    )
    public ResponseEntity<List<AddressResponse>> getUserAddresses() {

        User user = authUtil.getLoggedInUser();
        var response = addressService.getAddressesByUser(user);
        return ResponseEntity.ok(response);

    }

    @PutMapping("/user/addresses/{id}")
    @Operation(
        summary = "Update an address",
        description = "Updates an existing address with new information"
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = AddressResponse.class)
        )
    )
    public ResponseEntity<AddressResponse> updateAddress(@Parameter(description = "Address ID") @PathVariable String id,
        @Valid @RequestBody AddressRequest request) {

        var response = addressService.updateAddress(Long.parseLong(id), request);
        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/user/addresses/{id}")
    @Operation(
        summary = "Delete an address",
        description = "Permanently deletes the specified address"
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = MessageResponse.class)
        )
    )
    public ResponseEntity<MessageResponse> deleteAddress(@Parameter(description = "Address ID") @PathVariable Long id) {

        addressService.deleteAddress(id);

        return ResponseEntity.ok(new MessageResponse("Address deleted successfully"));
    }

}
