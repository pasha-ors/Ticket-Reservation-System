package com.event_driven.ticket.dto;

import com.event_driven.ticket.model.seat.SeatStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationResponse(
        UUID seatId,
        SeatStatus status,
        LocalDateTime reservedAt
) {}
