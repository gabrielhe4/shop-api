package io.github.gabrielhe4.shop_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.gabrielhe4.shop_api.dto.OrderDTO;
import io.github.gabrielhe4.shop_api.dto.OrderRequestDTO;
import io.github.gabrielhe4.shop_api.service.OrderService;
import io.github.gabrielhe4.shop_api.util.AuthUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;
    private final AuthUtil authUtil;

    OrderController(OrderService orderService, AuthUtil authUtil) { 
        this.orderService = orderService;
        this.authUtil = authUtil; 
    }

    @PostMapping("/user/order/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable String paymentMethod, @RequestBody OrderRequestDTO orderRequestDTO) {
        String email = authUtil.getLoggedInEmail();
        var response = orderService.placeOrder(email, paymentMethod, orderRequestDTO);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    
}
