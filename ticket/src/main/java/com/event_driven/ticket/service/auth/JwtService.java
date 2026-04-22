package com.event_driven.ticket.service.auth;

import com.event_driven.ticket.dto.auth.JwtDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.UUID;
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(JwtDTO data) {
        return Jwts.builder()
                .setSubject(data.username())
                .claim("userId", data.userId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getKey())
                .compact();
    }

    public JwtDTO parseToken(String token) {
        var claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return new JwtDTO(
                UUID.fromString(claims.get("userId", String.class)),
                claims.getSubject()
        );
    }

    public long getExpirationTime() {
        return EXPIRATION_TIME / 1000;
    }
}
