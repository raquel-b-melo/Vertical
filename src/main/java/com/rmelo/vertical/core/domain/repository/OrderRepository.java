package com.rmelo.vertical.core.domain.repository;

import com.rmelo.vertical.core.domain.model.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByPurchaseDateBetween(LocalDate startDate, LocalDate endDate);

    @EntityGraph(attributePaths = {"products"})
    Optional<Order> findByOrderId(int orderId);
}
