package com.rmelo.vertical.application.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.rmelo.vertical.core.domain.model.Logons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(Authentication authentication){
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof Logons logonUser)) {
            log.error("O principal da autenticação não é do tipo Logons. Encontrado: {}",
                    principal != null ? principal.getClass().getName() : "null");
            throw new IllegalArgumentException("Tipo de principal inválido para geração de token.");
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(logonUser.getLogin())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            log.error("Erro ao gerar token JWT para o usuário: {}", logonUser.getLogin(), exception);
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public Instant genExpirationDate() {
        return Instant.now().plus(1, ChronoUnit.HOURS);
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm).withIssuer("auth-api").build().verify(token).getSubject();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Token inválido ou expirado: " + e);
        }
    }
}
