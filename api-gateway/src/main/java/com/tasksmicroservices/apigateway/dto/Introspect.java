package com.tasksmicroservices.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Introspect {

    public String id;

    @JsonProperty("user_id")
    public String userId;

    public String scope;

    public String createdAt;

    public String expiresAt;

    public Introspect() {

    }

    public Introspect(String id, String userId, String scope, String createdAt, String expiresAt) {
        this.id = id;
        this.userId = userId;
        this.scope = scope;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public String toString() {
        return "Introspect{" +
                "id=" + id +
                ", user='" + userId + '\'' +
                ", scope='" + scope + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
