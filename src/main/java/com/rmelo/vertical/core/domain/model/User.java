package com.rmelo.vertical.core.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Table(name = "users")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(length = 45, nullable = false)
    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Order> orders;

    public User(int userId, String name, Set<Order> orders) {
        this.userId = userId;
        this.name = name;
        this.orders = orders;
    }

    public User(int userId, String name) {
        this.userId = userId;
        this.name = name;
        this.orders = Set.of();
    }

}