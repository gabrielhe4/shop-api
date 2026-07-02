package io.github.gabrielhe4.shop_api.security;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import io.github.gabrielhe4.shop_api.security.service.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${project.jwtSecret}")
    private String jwtSecret;

    @Value("${project.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${project.jwtCookieName}")
    private String jwtCookie;

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            log.debug("Cookie value: {}", cookie.getValue());
            return cookie.getValue();
        }
        return null;
    }

    public ResponseCookie cleanJwtCookie() {
        return ResponseCookie.from(jwtCookie, null).path("/api").build();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        return ResponseCookie.from(jwtCookie, jwt)
            .path("/api")
            .maxAge(24 * 60 * 60)
            .httpOnly(true)
            .build();
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(authToken);
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        }
        return false;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
    

}
