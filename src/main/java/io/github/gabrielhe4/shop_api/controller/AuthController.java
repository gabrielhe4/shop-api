package io.github.gabrielhe4.shop_api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.gabrielhe4.shop_api.dto.LoginRequest;
import io.github.gabrielhe4.shop_api.dto.MessageResponse;
import io.github.gabrielhe4.shop_api.dto.SignupRequest;
import io.github.gabrielhe4.shop_api.dto.UserInfoResponse;
import io.github.gabrielhe4.shop_api.service.AuthService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    
    public AuthController(AuthenticationManager authenticationManager, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest request) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
        } catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);

            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }

        var dto = authService.authenticateUser(authentication);
        String cookie = dto.cookie();

        var response = new UserInfoResponse(dto.id(), dto.username(), dto.roles());
        
        return ResponseEntity
            .ok() 
            .header(HttpHeaders.SET_COOKIE, cookie)
            .body(response);
    }

    @PostMapping("sign-up")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest request) {
        if (authService.checkIfUsernameExists(request.username()))
            return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Username is already taken!!!"));
        
        if(authService.checkIfEmailExists(request.email()))
            return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Email is already taken!!!"));

        authService.registerUser(request);
        
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @GetMapping("/username")
    public ResponseEntity<String> getCurrentUsername(Authentication auth) {

        if (auth == null)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Error: User not found");

        var response = authService.authenticateUser(auth);

        return ResponseEntity.ok().body(response.username());

    }

    @PostMapping("/sign-out")
    public ResponseEntity<MessageResponse> postMethodName(@RequestBody String entity) {
        var cleanedCookie = authService.cleanJwtCookie();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE,  cleanedCookie)
            .body(new MessageResponse("You've been signed out!"));
    }
    
    
    


    

}
