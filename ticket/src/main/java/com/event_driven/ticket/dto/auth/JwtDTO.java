package com.event_driven.ticket.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record JwtDTO(
        @NotNull
        UUID userId,

        @NotBlank
        String username) {
}
