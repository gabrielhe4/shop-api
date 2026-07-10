package io.github.gabrielhe4.shop_api.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.gabrielhe4.shop_api.dto.SignupRequest;
import io.github.gabrielhe4.shop_api.dto.UserInfoDTO;
import io.github.gabrielhe4.shop_api.enumeration.AppRole;
import io.github.gabrielhe4.shop_api.model.Role;
import io.github.gabrielhe4.shop_api.model.User;
import io.github.gabrielhe4.shop_api.repository.RoleRepository;
import io.github.gabrielhe4.shop_api.repository.UserRepository;
import io.github.gabrielhe4.shop_api.security.JwtUtils;
import io.github.gabrielhe4.shop_api.security.service.UserDetailsImpl;
import io.github.gabrielhe4.shop_api.service.AuthService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public Boolean checkIfUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean checkIfEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void registerUser(SignupRequest request) {
        User user = new User(request.username(), request.email(),
        passwordEncoder.encode(request.password()));

        Set<String> strRoles = request.roles();
        Set<Role> roles = new HashSet<>();  

         if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role not found"));

            roles.add(userRole);
        } else {
            // admin --> ROLE_ADMIN
            // seller --> ROLE_USER
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(adminRole);
                    }

                    case "seller" -> {
                        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                            .orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(sellerRole);
                    }

                    default -> {
                        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role not found"));

                        roles.add(userRole);
                    }
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public UserInfoDTO authenticateUser(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetailsImpl);

        List<String> roles = userDetailsImpl.getAuthorities()
                                .stream()
                                .map(item -> item.getAuthority())
                                .toList();
        
        return new UserInfoDTO(userDetailsImpl.getId(),
            userDetailsImpl.getUsername(),
            roles,
            jwtCookie.toString()
        );
    }

    @Override
    public String cleanJwtCookie() {
        return jwtUtils.cleanJwtCookie().toString();
    }


}
