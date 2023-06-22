package com.tasksmicroservices.authservice.repository;

import com.tasksmicroservices.authservice.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE user_details SET deleted_at = NOW() WHERE id = :id", nativeQuery = true)
    void softDeleteById(@Param("id") UUID id);

}
