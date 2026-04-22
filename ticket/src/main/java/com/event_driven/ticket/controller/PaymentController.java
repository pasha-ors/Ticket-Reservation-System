package com.event_driven.ticket.controller;

import com.event_driven.ticket.dto.PaymentRequest;
import com.event_driven.ticket.dto.ReservationRequest;
import com.event_driven.ticket.dto.ReservationResponse;
import com.event_driven.ticket.model.CustomUserDetails;
import com.event_driven.ticket.model.seat.Seat;
import com.event_driven.ticket.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {


    private final PaymentService paymentService;

    @PostMapping
    public ReservationResponse pay(@RequestBody PaymentRequest request, @AuthenticationPrincipal CustomUserDetails user)
    {
        Seat seat = paymentService.processPayment(
                request,
                user.getId()
        );

        return new ReservationResponse(
            seat.getId(),
            seat.getStatus(),
            seat.getReservedAt()
        );
    }
}
