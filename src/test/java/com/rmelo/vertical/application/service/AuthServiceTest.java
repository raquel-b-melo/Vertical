package com.rmelo.vertical.application.service;

import com.rmelo.vertical.core.domain.model.Logons;
import com.rmelo.vertical.core.domain.model.dto.RegisterDTO;
import com.rmelo.vertical.core.domain.model.enums.Roles;
import com.rmelo.vertical.core.domain.repository.LogonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private LogonRepository logonRepository;

    @InjectMocks
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        authService = new AuthService(logonRepository,passwordEncoder);
    }

    @Test
    void shouldLoadUserByUsernameSuccessfully() {
        Logons mockUser = new Logons("testUser", "password123", Roles.ADMIN);
        when(logonRepository.findByLogin("testUser")).thenReturn(mockUser);

        UserDetails userDetails = authService.loadUserByUsername("testUser");

        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(logonRepository.findByLogin("unknownUser")).thenReturn(null);

        UserDetails userDetails = authService.loadUserByUsername("unknownUser");

        assertNull(userDetails);
        verify(logonRepository, times(1)).findByLogin("unknownUser");

    }

    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterDTO registerDTO = new RegisterDTO("newUser", "securePassword", Roles.ADMIN);

        when(passwordEncoder.encode(registerDTO.password())).thenReturn("encodedPassword123");

        authService.register(registerDTO);

        verify(logonRepository, times(1)).save(any(Logons.class));
    }
}