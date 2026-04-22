package com.event_driven.ticket.controller;

import com.event_driven.ticket.dto.ReservationRequest;
import com.event_driven.ticket.dto.ReservationResponse;
import com.event_driven.ticket.dto.SeatResponse;
import com.event_driven.ticket.model.CustomUserDetails;
import com.event_driven.ticket.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/seats")
    public List<SeatResponse> getSeat(@RequestParam String row) throws Exception
    {
        return bookingService.getAvailableSeatsInRow(row)
                .stream()
                .map(seat -> new SeatResponse(
                        seat.getId(),
                        seat.getRowNumber(),
                        seat.getSeatNumber(),
                        seat.getStatus()
                )).toList();
    }

    @PostMapping("/reserve")
    public ReservationResponse reserve(@RequestBody ReservationRequest request, @AuthenticationPrincipal CustomUserDetails user) throws Exception
    {
        return bookingService.reserveSeat(request, user.getId());
    }
}
