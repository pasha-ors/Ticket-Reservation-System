package com.event_driven.ticket.service;

import com.event_driven.ticket.model.event.PaymentConfirmedEvent;
import com.event_driven.ticket.model.event.SeatReservedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final ApplicationEventPublisher eventPublisher;

    @EventListener
    public void handleSeatReservedEvent(SeatReservedEvent event){
        System.out.println("[PaymentService] Waiting for payment from the user: " + event.userId());
    }

    public void processSuccessfulPayment(UUID seatId) {
        System.out.println("[PaymentService] Payment received for the venue: " + seatId);
        eventPublisher.publishEvent(new PaymentConfirmedEvent(seatId));
    }
}
