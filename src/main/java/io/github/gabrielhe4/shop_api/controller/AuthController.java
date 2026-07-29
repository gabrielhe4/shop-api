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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User authentication and registration operations")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    public AuthController(AuthenticationManager authenticationManager, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    @PostMapping("/sign-in")
    @Operation(
        summary = "Authenticate user and return JWT cookie",
        description = "Logs in a user and returns an access token cookie along with user information. Requires username and password."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful authentication"),
        @ApiResponse(responseCode = "401", description = "Bad credentials (invalid username or password)")
    })
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
    @Operation(
        summary = "Register a new user",
        description = "Creates a new account with the provided username and email. Validates that username and email are unique."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User registered successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Username or Email already taken",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
        )
    })
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
    @Operation(
        summary = "Get current authenticated user's username",
        description = "Returns the username of the currently authenticated user. Returns 204 No Content if not authenticated."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success - returns username",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(responseCode = "204", description = "User not authenticated",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
        )
    })
    public ResponseEntity<String> getCurrentUsername(Authentication auth) {

        if (auth == null)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Error: User not found");

        var username = auth.getName();

        return ResponseEntity.ok().body(username);

    }

    @PostMapping("/sign-out")
    @Operation(
        summary = "Sign out user",
        description = "Invalidates the JWT cookie and clears any stored tokens. This effectively logs out the user."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully signed out",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    public ResponseEntity<MessageResponse> signOut() {
        var cleanedCookie = authService.cleanJwtCookie();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE,  cleanedCookie)
            .body(new MessageResponse("You've been signed out!"));
    }







}
