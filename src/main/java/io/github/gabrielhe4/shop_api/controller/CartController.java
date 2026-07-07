package io.github.gabrielhe4.shop_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.gabrielhe4.shop_api.dto.CartDTO;
import io.github.gabrielhe4.shop_api.service.CartService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId, 
        @PathVariable Integer quantity) {
        
        var response = cartService.addProductToCart(productId, quantity);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getCarts() {
        
        List<CartDTO> carts = cartService.getAllCarts();

        return ResponseEntity.ok(carts);
    }

    @GetMapping("/carts/user/cart/")
    public ResponseEntity<CartDTO> getCartById() {
        
        var response = cartService.getCart();

        return ResponseEntity.ok(response);

    }

    // in operation send add -> to add :P
    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId,
            @PathVariable String operation) {

        var response = cartService.updateProductQuantityInCart(productId,
                operation.equalsIgnoreCase("delete") ? -1 : 1);

        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long cartId,
            @PathVariable Long productId) {

        String status = cartService.deleteProductFromCart(cartId, productId);

        return ResponseEntity.ok(status);

    }
    
    
    
}
