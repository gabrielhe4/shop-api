package io.github.gabrielhe4.shop_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.gabrielhe4.shop_api.util.AuthUtil;
import jakarta.validation.Valid;
import io.github.gabrielhe4.shop_api.dto.AddressDTO;
import io.github.gabrielhe4.shop_api.dto.AddressRequest;
import io.github.gabrielhe4.shop_api.model.User;
import io.github.gabrielhe4.shop_api.service.AddressService;

@RestController
@RequestMapping("/api")
public class AddressController {

    private final AuthUtil authUtil;
    private final AddressService addressService;
    
    public AddressController(AuthUtil authUtil, AddressService addressService) {
        this.authUtil = authUtil;
        this.addressService = addressService;
    }

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createdAddress(@Valid @RequestBody AddressRequest request) {

        User user = authUtil.getLoggedInUser();
        var response = addressService.create(request, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
        
    }

    


}
