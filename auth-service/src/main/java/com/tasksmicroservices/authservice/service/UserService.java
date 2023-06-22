package com.tasksmicroservices.authservice.service;

import com.tasksmicroservices.authservice.dto.AuthRequest;
import com.tasksmicroservices.authservice.model.User;
import com.tasksmicroservices.authservice.repository.AccessTokenRepository;
import com.tasksmicroservices.authservice.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setDeletedAt(null);
        user.setScope("profile todo:read todo:write todo:delete");
        return userRepository.save(user);
    }

    public void deleteById(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if(!user.isEmpty() && user.get().getScope().contains("profile")) {
            userRepository.softDeleteById(id);
            accessTokenRepository.deleteByUserId(id);
        }
    }

}
