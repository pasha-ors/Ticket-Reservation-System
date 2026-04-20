package com.event_driven.ticket;

import com.event_driven.ticket.model.seat.Seat;
import com.event_driven.ticket.model.seat.SeatStatus;
import com.event_driven.ticket.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final SeatRepository seatRepository;

    @Override
    public void run(String... args) throws Exception {

        if(seatRepository.count() == 0){
            for(int i = 1; i <= 10; i++){
                Seat seat = new Seat();
                seat.setRowNumber("A1");
                seat.setSeatNumber(String.valueOf(i));
                seat.setStatus(SeatStatus.AVAILABLE);
                seatRepository.save(seat);
            }
        }
        System.out.println("✅ H2 Database initialized with 10 seats in row A1");
    }
}
