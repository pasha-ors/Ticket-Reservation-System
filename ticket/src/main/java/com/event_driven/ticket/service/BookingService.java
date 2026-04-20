package com.event_driven.ticket.service;

import com.event_driven.ticket.model.event.PaymentConfirmedEvent;
import com.event_driven.ticket.model.event.SeatReservedEvent;
import com.event_driven.ticket.model.seat.Seat;
import com.event_driven.ticket.model.seat.SeatStatus;
import com.event_driven.ticket.repository.SeatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final SeatRepository seatRepository;
    private final ApplicationEventPublisher eventPublisher;

    public List<Seat> getAvailableSeatsInRow(String rowNumber) throws Exception {
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);

        return seatRepository.findByRowNumberOrderBySeatNumberAsc(rowNumber)
                    .stream()
                    .filter(s -> isSeatActuallyAvailable(s, tenMinutesAgo))
                    .toList();
    }

    @Transactional
    public void reserveSeat(UUID seatId, UUID userId){
        Seat seat = seatRepository.findById(seatId).orElseThrow(()-> new RuntimeException("Seat not found"));

        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);

        if (!isSeatActuallyAvailable(seat, tenMinutesAgo)){
            throw new RuntimeException("[Booking Service] Seat is already reserved");
        }

        seat.setStatus(SeatStatus.RESERVED);
        seat.setReservedAt(LocalDateTime.now());
        seat.setReservedByUserId(userId);

        seatRepository.save(seat);

        eventPublisher.publishEvent(new SeatReservedEvent(seatId, userId));
    }

    @EventListener
    @Transactional
    public void handlePaymentConfirmed(PaymentConfirmedEvent event) {
        Seat seat = seatRepository.findById(event.seatId())
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        seat.setStatus(SeatStatus.SOLD);
        seatRepository.save(seat);
        System.out.println("[Booking Service] Seat is SOLD: " + seat.getSeatNumber());
    }

    private boolean isSeatActuallyAvailable(Seat seat, LocalDateTime threshold){
        if(seat.getStatus() == SeatStatus.AVAILABLE){
            return true;
        }

        return seat.getStatus() == SeatStatus.RESERVED
                && seat.getReservedAt().isBefore(threshold);
    }

    @Scheduled(fixedRate = 30000)
    @Transactional
    public void releaseExpiredReservations() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(10);
        seatRepository.releaseExpiredSeats(threshold);
        System.out.println("[Booking Service] SQL Cleaner");
    }
}
