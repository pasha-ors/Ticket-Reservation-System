package com.event_driven.ticket.service;

import com.event_driven.ticket.dto.PaymentRequest;
import com.event_driven.ticket.model.seat.Seat;
import com.event_driven.ticket.model.seat.SeatStatus;
import com.event_driven.ticket.repository.SeatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PaymentService {

    private final SeatRepository seatRepository;

    @Transactional
    public Seat processPayment(PaymentRequest request, UUID userId) {

        Seat seat = seatRepository.findById(request.seatId())
                .orElseThrow(() -> new RuntimeException("Seat Not Found"));

        if(seat.getStatus() != SeatStatus.RESERVED) {
            throw new RuntimeException("Seat Status Not Reserved");
        }

        if(!seat.getReservedByUserId().equals(userId)) {
            throw new RuntimeException("Not your reservation");
        }

        if(seat.getReservedAt() == null ||
            seat.getReservedAt().isBefore(LocalDateTime.now().minusMinutes(10))){
            throw new RuntimeException("Reservation expired");
        }

        seat.setStatus(SeatStatus.SOLD);
        seat.setReservedAt(null);
        seat.setReservedByUserId(null);

        return seatRepository.save(seat);
    }
}
