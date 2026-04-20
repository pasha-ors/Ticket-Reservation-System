package com.event_driven.ticket.controller;

import com.event_driven.ticket.model.dto.ReservationRequestDTO;
import com.event_driven.ticket.model.dto.SeatResponseDTO;
import com.event_driven.ticket.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/seats")
    public List<SeatResponseDTO> getSeat(@RequestParam String row) throws Exception
    {
        return bookingService.getAvailableSeatsInRow(row)
                .stream()
                .map(seat -> new SeatResponseDTO(
                        seat.getId(),
                        seat.getRowNumber(),
                        seat.getSeatNumber(),
                        seat.getStatus()
                )).toList();
    }

    @PostMapping("/reserve")
    public String reserve(@RequestBody ReservationRequestDTO request) throws Exception
    {
        bookingService.reserveSeat(request.seatId(), request.userId());
        return "[Booking Controller] Seat reserved: " + request.seatId();
    }

}
