package com.loopers.domain.order;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface OrderRepository extends Repository<Order, Long> {
    Order save(Order order);

    Optional<Order> findById(Long orderId);
}
