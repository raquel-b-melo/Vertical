package com.rmelo.vertical.application.service;

import com.rmelo.vertical.core.domain.model.Logons;
import com.rmelo.vertical.core.domain.model.enums.Roles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", "test-secret");
    }

    @Test
    void shouldGenerateTokenSuccessfully() {
        Logons logonUser = new Logons("validUser", "password", Roles.ADMIN); // Simulando um usuário válido
        when(authentication.getPrincipal()).thenReturn(logonUser);

        String token = tokenService.generateToken(authentication);

        assertNotNull(token);
    }

    @Test
    void shouldThrowExceptionWhenPrincipalIsInvalid() {
        when(authentication.getPrincipal()).thenReturn("InvalidPrincipal");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            tokenService.generateToken(authentication);
        });

        assertEquals("Tipo de principal inválido para geração de token.", exception.getMessage());
    }

    @Test
    void shouldValidateTokenSuccessfully() {
        Logons logonUser = new Logons("validUser", "password", Roles.ADMIN);
        when(authentication.getPrincipal()).thenReturn(logonUser);

        String token = tokenService.generateToken(authentication);
        String subject = tokenService.validateToken(token);

        assertEquals("validUser", subject);
    }

    @Test
    void shouldThrowExceptionForInvalidToken() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            tokenService.validateToken("invalidToken");
        });

        assertTrue(exception.getMessage().contains("Token inválido ou expirado"));
    }
}