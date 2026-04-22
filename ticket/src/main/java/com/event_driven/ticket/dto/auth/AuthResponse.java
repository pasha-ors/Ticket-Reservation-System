package com.event_driven.ticket.dto.auth;

public record AuthResponse(
        String token,
        String type,
        long expiresIn
) {}
