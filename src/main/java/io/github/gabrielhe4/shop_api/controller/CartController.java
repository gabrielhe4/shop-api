package io.github.gabrielhe4.shop_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.gabrielhe4.shop_api.dto.CartResponse;
import io.github.gabrielhe4.shop_api.dto.CartRequest;
import io.github.gabrielhe4.shop_api.service.CartService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api")
@Tag(name = "Cart Operations", description = "Shopping cart management endpoints")
@OpenAPIDefinition(info = @Info(title = "Shop API - Cart Operations", version = "1.0.0"))
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/cart/products")
    @Operation(
        summary = "Add product to shopping cart",
        description = "Adds a product to the user's shopping cart with the specified quantity"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product successfully added to cart",
            content = @Content(schema = @Schema(implementation = CartResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request - missing or invalid fields"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<CartResponse> addProductToCart(
        @Valid @RequestBody CartRequest request
    ) {

        var response = cartService.addProductToCart(request.productId(), request.quantity());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("user/cart")
    @Operation(
        summary = "Get shopping cart",
        description = "Retrieves the current state of the user's shopping cart with all items and total price"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart retrieved successfully",
            content = @Content(schema = @Schema(implementation = CartResponse.class))),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    public ResponseEntity<CartResponse> getCartById() {

        var response = cartService.getCart();

        return ResponseEntity.ok(response);

    }

    // in operation send add -> to add
    @PutMapping("/user/cart/products/{productId}")
    @Operation(
        summary = "Update product quantity in cart",
        description = "Adds or removes items from the shopping cart for a specific product\n" +
            "Send 'add' to add an item (quantity +1), or 'delete' to remove (quantity -1)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart updated successfully",
            content = @Content(schema = @Schema(implementation = CartResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid operation parameter"),
        @ApiResponse(responseCode = "404", description = "Product not found or cart not found")
    })
    public ResponseEntity<CartResponse> updateCartProduct(@PathVariable Long productId,
            @RequestParam("operation") String operation) {

        var response = cartService.updateProductQuantityInCart(productId,
                operation.equalsIgnoreCase("delete") ? -1 : 1);

        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/cart/products/{productId}")
    @Operation(
        summary = "Delete product from shopping cart",
        description = "Completely removes a product from the user's shopping cart"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product successfully deleted from cart",
            content = @Content(schema = @Schema(implementation = String.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Product not found in cart")
    })
    public ResponseEntity<String> deleteProduct(
            @PathVariable Long productId) {

        String status = cartService.deleteProductFromCart(productId);

        return ResponseEntity.ok(status);

    }



}
