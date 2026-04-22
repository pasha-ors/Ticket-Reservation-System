package com.event_driven.ticket;

import com.event_driven.ticket.model.seat.Seat;
import com.event_driven.ticket.model.seat.SeatStatus;
import com.event_driven.ticket.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final SeatRepository seatRepository;

    @Bean
    CommandLineRunner initSeats() {
        return args -> {

            if (seatRepository.count() > 0) return;

            for (int i = 1; i <= 10; i++) {
                Seat seat = new Seat();
                seat.setRowNumber("A1");
                seat.setSeatNumber(String.valueOf(i));
                seat.setStatus(SeatStatus.AVAILABLE);

                seatRepository.save(seat);
            }

            System.out.println(">>> Seats initialized");
        };
    }
}