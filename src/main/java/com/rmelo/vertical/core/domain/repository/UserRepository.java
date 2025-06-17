package com.rmelo.vertical.core.domain.repository;

import com.rmelo.vertical.core.domain.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @EntityGraph(attributePaths = {"orders", "orders.products"})
    @Query("SELECT u FROM User u")
    List<User> findAllWithOrdersAndProducts();
}
