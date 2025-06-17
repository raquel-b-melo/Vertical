package com.rmelo.vertical.core.domain.repository;

import com.rmelo.vertical.core.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
