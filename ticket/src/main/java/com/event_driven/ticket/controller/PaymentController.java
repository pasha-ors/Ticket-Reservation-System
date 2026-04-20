package com.event_driven.ticket.controller;

import com.event_driven.ticket.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/confirm")
    public String confirmPayment(@RequestParam UUID seatId){
        paymentService.processSuccessfulPayment(seatId);
        return "[Payment Controller] Payment processed for seat: " + seatId;
    }
}
