package com.tasksmicroservices.authservice.controller;

import com.tasksmicroservices.authservice.dto.UserInfo;
import com.tasksmicroservices.authservice.model.AccessToken;
import com.tasksmicroservices.authservice.model.User;
import com.tasksmicroservices.authservice.service.AuthService;
import com.tasksmicroservices.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @GetMapping("/user-info")
    public ResponseEntity<Object> getUserInfo(@RequestHeader("Authorization") String authorization) {
        String token = authService.validateAuthorization(authorization);
        if(token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token format.");
        }
        try {
            User user = authService.introspect(token).getUser();
            if (user.getScope().contains("profile")) {
                return ResponseEntity.ok().body(
                        new UserInfo(user.getId(), user.getEmail(), user.getName(), user.getScope(), user.getCreatedAt(), user.getUpdatedAt())
                );
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You're not authorized to access this resource.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<Object> deleteUser(@RequestHeader("Authorization") String authorization) {
        String token = authService.validateAuthorization(authorization);
        if(token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token format.");
        }
        try {
            AccessToken accessToken = authService.introspect(token);
            userService.deleteById(accessToken.getUser().getId());
            return ResponseEntity.ok().body("User deleted successfully!");
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
