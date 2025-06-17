package com.rmelo.vertical.core.domain.repository;

import com.rmelo.vertical.core.domain.model.Logons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface LogonRepository extends JpaRepository<Logons, UUID> {
    UserDetails findByLogin(String login);
}
