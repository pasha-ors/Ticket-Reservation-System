package com.event_driven.ticket;

import com.event_driven.ticket.model.order.OrderStatus;
import com.event_driven.ticket.model.seat.Seat;
import com.event_driven.ticket.model.seat.SeatStatus;
import com.event_driven.ticket.repository.OrderRepository;
import com.event_driven.ticket.repository.SeatRepository;
import com.event_driven.ticket.service.BookingService;
import com.event_driven.ticket.service.PaymentService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BookingTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("1. The reservation should be successful and the order should be created")
    void shouldReserveSeatAndCreatePendingOrder() {

        Seat seat = seatRepository.findAll().get(0);

        UUID userID = UUID.randomUUID();

        bookingService.reserveSeat(seat.getId(), userID);

        assertEquals(SeatStatus.RESERVED, seatRepository.findById(seat.getId()).get().getStatus());

        assertEquals(1, orderRepository.findAll().size());

        assertEquals(OrderStatus.PENDING, orderRepository.findAll().get(0).getStatus());
    }

    @Test
    @DisplayName("2. It should display an error when attempting to reserve a seat that is already taken")
    void shouldThrowExceptionWhenReservingAlreadyReservedSeat() {

        UUID user1ID = UUID.randomUUID();
        UUID user2ID = UUID.randomUUID();

        Seat seat = seatRepository.findAll().get(0);

        bookingService.reserveSeat(seat.getId(), user1ID);

        assertThrows(RuntimeException.class, () -> bookingService.reserveSeat(seat.getId(), user2ID));
    }

    @Test
    @DisplayName("3. The status should change to SOLD (Seat) and PAID (Order) after payment")
    void shouldUpdateStatusesAfterPayment() {
        UUID user1ID = UUID.randomUUID();
        Seat seat = seatRepository.findAll().get(0);

        bookingService.reserveSeat(seat.getId(), user1ID);

        paymentService.processSuccessfulPayment(seat.getId());

        assertEquals(OrderStatus.PAID, orderRepository.findAll().get(0).getStatus());
        assertEquals(SeatStatus.SOLD, seatRepository.findById(seat.getId()).get().getStatus());
    }

    @Test
    @DisplayName("4. The scheduler must release overdue reservations")
    void shouldReleaseExpiredReservations() {
        Seat seat = seatRepository.findAll().get(0);
        seat.setStatus(SeatStatus.RESERVED);

        seat.setReservedAt(LocalDateTime.now().minusMinutes(15));
        seat.setReservedByUserId(UUID.randomUUID());
        seatRepository.saveAndFlush(seat);

        bookingService.releaseExpiredReservations();

        Seat updatedSeat = seatRepository.findById(seat.getId()).get();

        assertEquals(SeatStatus.AVAILABLE, updatedSeat.getStatus());
        assertNull(updatedSeat.getReservedAt());
        assertNull(updatedSeat.getReservedByUserId());
    }

}
