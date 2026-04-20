package com.event_driven.ticket.model.dto;

import com.event_driven.ticket.model.seat.SeatStatus;

import java.util.UUID;

public record SeatResponseDTO (
    UUID id,
    String rowNumber,
    String seatNumber,
    SeatStatus status
) {}
