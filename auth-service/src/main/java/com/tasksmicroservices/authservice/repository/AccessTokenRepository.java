package com.tasksmicroservices.authservice.repository;

import com.tasksmicroservices.authservice.model.AccessToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.UUID;

public interface AccessTokenRepository extends JpaRepository<AccessToken, UUID> {

    @Transactional
    @Modifying(clearAutomatically = true)
    void deleteByUserId(UUID userId);

}
