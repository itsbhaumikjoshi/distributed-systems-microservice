package com.tasksmicroservices.authservice.service;

import com.tasksmicroservices.authservice.model.AccessToken;
import com.tasksmicroservices.authservice.dto.AuthRequest;
import com.tasksmicroservices.authservice.dto.AuthResponse;
import com.tasksmicroservices.authservice.model.User;
import com.tasksmicroservices.authservice.repository.AccessTokenRepository;
import com.tasksmicroservices.authservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

@Component
public class AuthService {

    private static Integer EXPIRES_IN = 60 * 60 * 24;

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public AuthResponse signUpUser(User user) {
        return createToken(user);
    }

    public AuthResponse signInUser(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );
        User user = userRepository.findByEmail(authRequest.getEmail()).orElse(null);
        if (user == null || user.getDeletedAt() != null) {
            return null;
        }
        return createToken(user);
    }

    public void logout(String token) throws Exception {
        AccessToken accessToken = introspect(token);
        accessTokenRepository.deleteById(accessToken.getId());
    }

    public AccessToken introspect(String token) throws Exception {
        // if token has expired throw error.
        Claims claims = validate(token);
        AccessToken accessToken = accessTokenRepository.findById(
                UUID.fromString(
                        (String) claims.get("jti")
                )
        ).orElse(null);
        // if token is valid and not in the database throw error. JWT in revocable property.
        if (accessToken == null) {
            throw new RuntimeException("Invalid token.");
        }
        return accessToken;
    }

    public String validateAuthorization(String authorization) {
        if(!authorization.startsWith("Bearer ")) {
            return null;
        }
        String[] authorizationFormat = authorization.split(" ");
        return authorizationFormat[1];
    }

    private Claims validate(String token) {
        return jwtService.validateAndClaims(token);
    }

    private AuthResponse createToken(User user) {
        AccessToken accessToken = new AccessToken();
        accessToken.setUser(user);
        accessToken.setCreatedAt(LocalDateTime.now());
        accessToken.setExpiresAt(LocalDateTime.now().plusSeconds(EXPIRES_IN));
        AccessToken createdAccessToken = accessTokenRepository.save(accessToken);
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("jti", createdAccessToken.getId().toString());
        claims.put("email", user.getEmail());
        claims.put("scope", user.getScope());
        String token = jwtService.create(user, claims, EXPIRES_IN * 1000);
        return new AuthResponse(token, EXPIRES_IN);
    }

}
