package com.tasksmicroservices.authservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tasksmicroservices.authservice.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class Introspect {

    public UUID id;

    @JsonProperty("user_id")
    public String user;

    public String scope;

    public LocalDateTime createdAt;

    public LocalDateTime expiresAt;

    public Introspect(UUID id, String user, String scope, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.id = id;
        this.user = user;
        this.scope = scope;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public String toString() {
        return "Introspect{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", scope='" + scope + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
