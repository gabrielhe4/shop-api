package io.github.gabrielhe4.shop_api.service;

import org.springframework.security.core.Authentication;

import io.github.gabrielhe4.shop_api.dto.SignupRequest;
import io.github.gabrielhe4.shop_api.dto.UserInfoDTO;

public interface AuthService {

    Boolean checkIfUsernameExists(String username);

    Boolean checkIfEmailExists(String email);

    void registerUser(SignupRequest request);

    UserInfoDTO authenticateUser(Authentication  authentication);

    String cleanJwtCookie();

}
