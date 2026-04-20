package com.event_driven.ticket.model.event;

import java.util.UUID;

public record PaymentConfirmedEvent(UUID seatId) { }
