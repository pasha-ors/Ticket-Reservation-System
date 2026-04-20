package com.event_driven.ticket.service;

import com.event_driven.ticket.model.event.PaymentConfirmedEvent;
import com.event_driven.ticket.model.event.SeatReservedEvent;
import com.event_driven.ticket.model.order.Order;
import com.event_driven.ticket.model.order.OrderStatus;
import com.event_driven.ticket.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @EventListener
    @Transactional
    public void handleSeatReserved(SeatReservedEvent event) {

        Order order = new Order();
        order.setSeatId(event.seatId());
        order.setUserId(event.userId());
        order.setAmount(new BigDecimal("100.00"));
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        orderRepository.save(order);

        System.out.println("[Order Service] Order has been saved");
    }

    @EventListener
    @Transactional
    public void handlePaymentConfirmed(PaymentConfirmedEvent event){
        Order order = orderRepository.findFirstBySeatIdAndStatusOrderByCreatedAtDesc(
                event.seatId(),
                OrderStatus.PENDING
        ).orElseThrow(() -> new RuntimeException("Pending order not found for seat: " + event.seatId()));

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        System.out.println("[Order Service] Order: " + order.getId() + " is PAID");
    }

    @Transactional
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }
}
