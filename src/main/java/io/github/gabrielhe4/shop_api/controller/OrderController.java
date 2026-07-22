package io.github.gabrielhe4.shop_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.gabrielhe4.shop_api.dto.OrderResponse;
import io.github.gabrielhe4.shop_api.dto.OrderRequest;
import io.github.gabrielhe4.shop_api.service.OrderService;
import io.github.gabrielhe4.shop_api.util.AuthUtil;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
@Tag(name = "Orders", description = "Place and manage orders with payment methods")
@OpenAPIDefinition(info = @Info(title = "Shop API - Order Operations", version = "1.0.0"))
public class OrderController {

    private final OrderService orderService;
    private final AuthUtil authUtil;

    OrderController(OrderService orderService, AuthUtil authUtil) { 
        this.orderService = orderService;
        this.authUtil = authUtil; 
    }

    @PostMapping("/user/order/payments/{paymentMethod}")
    @Operation(
        summary = "Place an order with payment",
        description = "Places a new order for the authenticated user using the specified payment method. " +
            "Includes address ID and payment gateway details."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order successfully created", 
            content = @Content(schema = @Schema(implementation = OrderResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request - missing or invalid fields"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "404", description = "Address or payment method not found")
    })
    public ResponseEntity<OrderResponse> orderProducts(@PathVariable String paymentMethod, @RequestBody OrderRequest orderRequest) {
        String email = authUtil.getLoggedInEmail();
        var response = orderService.placeOrder(email, paymentMethod, orderRequest);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    
}
