package com.tasksmicroservices.authservice.controller;

import com.tasksmicroservices.authservice.dto.Introspect;
import com.tasksmicroservices.authservice.model.AccessToken;
import com.tasksmicroservices.authservice.dto.AuthRequest;
import com.tasksmicroservices.authservice.dto.AuthResponse;
import com.tasksmicroservices.authservice.model.User;
import com.tasksmicroservices.authservice.service.AuthService;
import com.tasksmicroservices.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/sign-in")
    public ResponseEntity<Object> signin(@RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.signInUser(authRequest);
        if (authResponse == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or Password is invalid.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> signup(@RequestBody User user) {
        User createdUser = userService.save(user);
        return ResponseEntity.ok().body(authService.signUpUser(createdUser));
    }

    @GetMapping("/logout")
    public ResponseEntity<Object> logout(@RequestHeader("Authorization") String authorization) {
        String token = authService.validateAuthorization(authorization);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token format.");
        }
        try {
            authService.logout(token);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/introspect")
    public ResponseEntity<Object> introspect(@RequestHeader("Authorization") String authorization) {
        String token = authService.validateAuthorization(authorization);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token format.");
        }
        try {
            AccessToken accessToken = authService.introspect(token);
            return ResponseEntity.ok().body(
                    new Introspect(accessToken.getId(), accessToken.getUser().getId().toString(), accessToken.getUser().getScope(), accessToken.getCreatedAt(), accessToken.getExpiresAt())
            );
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
