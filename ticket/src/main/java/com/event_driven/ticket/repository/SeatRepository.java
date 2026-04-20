package com.event_driven.ticket.repository;

import com.event_driven.ticket.model.seat.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SeatRepository extends JpaRepository<Seat, UUID> {

    List<Seat> findByRowNumberOrderBySeatNumberAsc(String rowNumber);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Seat s SET s.status = 'AVAILABLE', s.reservedAt = null, s.reservedByUserId = null " +
            "WHERE s.status = 'RESERVED' AND s.reservedAt < :threshold")
    int releaseExpiredSeats(@Param("threshold") LocalDateTime threshold);
}
