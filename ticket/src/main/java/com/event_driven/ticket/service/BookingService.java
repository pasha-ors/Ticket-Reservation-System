package com.event_driven.ticket.service;

import com.event_driven.ticket.dto.ReservationRequest;
import com.event_driven.ticket.dto.ReservationResponse;
import com.event_driven.ticket.model.seat.Seat;
import com.event_driven.ticket.model.seat.SeatStatus;
import com.event_driven.ticket.repository.SeatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BookingService {

    private final SeatRepository seatRepository;
    private final PaymentService paymentService;

    public List<Seat> getAvailableSeatsInRow(String rowNumber) throws Exception {
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);

        return seatRepository.findByRowNumberOrderBySeatNumberAsc(rowNumber)
                    .stream()
                    .filter(s -> isSeatActuallyAvailable(s, tenMinutesAgo))
                    .toList();
    }

    @Transactional
    public ReservationResponse reserveSeat(ReservationRequest request, UUID userId) {
        Seat seat = seatRepository.findById(request.seatId()).orElseThrow(()-> new RuntimeException("Seat not found"));

        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);

        if (!isSeatActuallyAvailable(seat, tenMinutesAgo)){
            throw new RuntimeException("Seat is already reserved");
        }

        seat.setStatus(SeatStatus.RESERVED);
        seat.setReservedAt(LocalDateTime.now());
        seat.setReservedByUserId(userId);

        try {
            seatRepository.save(seat);
        }catch (ObjectOptimisticLockingFailureException ex){
            throw new RuntimeException("Seat was just reserved by another user");
        }

        return new ReservationResponse(
                seat.getId(),
                seat.getStatus(),
                seat.getReservedAt()
        );
    }


    private boolean isSeatActuallyAvailable(Seat seat, LocalDateTime threshold){
        if(seat.getStatus() == SeatStatus.AVAILABLE){
            return true;
        }

        return seat.getStatus() == SeatStatus.RESERVED
                && seat.getReservedAt().isBefore(threshold);
    }

    @Scheduled(fixedRate = 30000, initialDelay = 30000)
    @Transactional
    public void releaseExpiredReservations() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(10);
        seatRepository.releaseExpiredSeats(threshold);
    }
}
