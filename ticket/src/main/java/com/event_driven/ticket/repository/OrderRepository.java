package com.event_driven.ticket.repository;

import com.event_driven.ticket.model.order.Order;
import com.event_driven.ticket.model.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findFirstBySeatIdAndStatusOrderByCreatedAtDesc(UUID seatId, OrderStatus status);
}
