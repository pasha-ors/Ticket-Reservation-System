package com.event_driven.ticket.model.dto;

import java.util.UUID;

public record ReservationRequestDTO(UUID seatId, UUID userId) {}
