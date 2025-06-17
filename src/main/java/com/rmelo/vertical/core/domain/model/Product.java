package com.rmelo.vertical.core.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "order_products")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "product_id", nullable = false)
    private int productId;

    @Column(name = "product_value", nullable = false, precision = 12, scale = 2)
    private BigDecimal value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id_fk", nullable = false)
    private Order order;

    public Product(int productId, BigDecimal value, Order order) {
        this.productId = productId;
        this.value = value;
        this.order = order;
    }
}