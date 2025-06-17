package com.rmelo.vertical.core.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Table(name = "orders")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "order_id", nullable = false)
    private int orderId;

    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_fk", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Product> products;

    public Order(int orderId, LocalDate purchaseDate, User user, Set<Product> products) {
        this.orderId = orderId;
        this.purchaseDate = purchaseDate;
        this.user = user;
        this.products = products;
    }

    public Order(int orderId, LocalDate purchaseDate, User user) {
        this.orderId = orderId;
        this.purchaseDate = purchaseDate;
        this.user = user;
        this.products = Set.of();;
    }
}