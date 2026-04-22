package com.event_driven.ticket.dto;

import com.event_driven.ticket.model.seat.SeatStatus;

import java.util.UUID;

public record SeatResponse(
        UUID id,
        String rowNumber,
        String seatNumber,
        SeatStatus seatStatus
) {
}
