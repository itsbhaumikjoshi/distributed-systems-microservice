package com.tasksmicroservices.authservice.service;

import com.tasksmicroservices.authservice.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

@Component
public class JwtService {

    @Value("${jwt.security.secret}")
    private String secretKey;

    public String create(User user, HashMap<String, Object> claims, Integer expiration) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSiginingKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateAndClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSiginingKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSiginingKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
