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
import io.github.gabrielhe4.shop_api.dto.AddressDTO;
import io.github.gabrielhe4.shop_api.dto.AddressRequest;
import io.github.gabrielhe4.shop_api.dto.MessageResponse;
import io.github.gabrielhe4.shop_api.model.User;
import io.github.gabrielhe4.shop_api.service.AddressService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




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

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAddresses() {
        
        var response = addressService.getAllAddresses();
        return ResponseEntity.ok(response);

    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) { 
        
        var response = addressService.getAddressById(addressId);
        return ResponseEntity.ok(response);

    }

    @GetMapping("user/address")
    public ResponseEntity<List<AddressDTO>> getUserAddresses() {

        User user = authUtil.getLoggedInUser();
        var response = addressService.getAddressesByUser(user);
        return ResponseEntity.ok(response);

    }

    @PutMapping("/addresses/{id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable String id, 
        @Valid @RequestBody AddressRequest request) {
        
        var response = addressService.updateAddress(Long.parseLong(id), request);
        return ResponseEntity.ok(response);
        
    }

    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<MessageResponse> deleteAddress(@PathVariable Long id) {
        
        addressService.deleteAddress(id);
        
        return ResponseEntity.ok(new MessageResponse("Address deleted successfully"));
    }
    
    

    


}
