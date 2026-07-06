package io.github.gabrielhe4.shop_api.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import io.github.gabrielhe4.shop_api.model.User;
import io.github.gabrielhe4.shop_api.repository.UserRepository;

@Component
public class AuthUtil {

    private final UserRepository userRepository;

    public AuthUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getLoggedInEMail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).orElseThrow(
            () -> new UsernameNotFoundException("User not found")
        );

        return user.getEmail(); 
    }

    public User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName()).orElseThrow(
            () -> new UsernameNotFoundException("User not found")
        );
    }

}
