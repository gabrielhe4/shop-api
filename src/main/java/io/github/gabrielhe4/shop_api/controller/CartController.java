package io.github.gabrielhe4.shop_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.gabrielhe4.shop_api.dto.CartDTO;
import io.github.gabrielhe4.shop_api.dto.CartRequest;
import io.github.gabrielhe4.shop_api.service.CartService;
import jakarta.validation.Valid;

import java.util.List;

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
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/cart/products")
    public ResponseEntity<CartDTO> addProductToCart(
        @Valid @RequestBody CartRequest request
    ) {
        
        var response = cartService.addProductToCart(request.productId(), request.quantity());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /*
    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getCarts() {
        
        List<CartDTO> carts = cartService.getAllCarts();

        return ResponseEntity.ok(carts);
    }
    */

    @GetMapping("/cart/user")
    public ResponseEntity<CartDTO> getCartById() {
        
        var response = cartService.getCart();

        return ResponseEntity.ok(response);

    }

    // in operation send add -> to add :P
    @PutMapping("/cart/products/{productId}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId,
            @RequestParam("operation") String operation) {

        var response = cartService.updateProductQuantityInCart(productId,
                operation.equalsIgnoreCase("delete") ? -1 : 1);

        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/cart/products/{productId}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable Long productId) {

        String status = cartService.deleteProductFromCart(productId);

        return ResponseEntity.ok(status);

    }
    
    
    
}
