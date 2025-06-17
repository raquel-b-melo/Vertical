package com.rmelo.vertical.infrastructure.rest.api;
import com.rmelo.vertical.application.service.AuthService;
import com.rmelo.vertical.application.service.TokenService;
import com.rmelo.vertical.core.domain.model.dto.AuthDTO;
import com.rmelo.vertical.core.domain.model.dto.LoginResponseDTO;
import com.rmelo.vertical.core.domain.model.dto.RegisterDTO;
import com.rmelo.vertical.core.domain.model.enums.Roles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthService authService;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController(authenticationManager, authService, tokenService);
    }

    @Test
    void shouldLoginSuccessfully() {
        AuthDTO authDTO = new AuthDTO("testUser", "securePassword");
        Authentication authenticationMock = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authenticationMock);
        when(tokenService.generateToken(authenticationMock)).thenReturn("mock-token");

        ResponseEntity<?> response = authController.login(authDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(LoginResponseDTO.class, response.getBody());
        assertEquals("mock-token", ((LoginResponseDTO) response.getBody()).token());
    }

    @Test
    void shouldRegisterSuccessfully() {
        RegisterDTO registerDTO = new RegisterDTO("newUser", "securePassword", Roles.ADMIN);

        when(authService.loadUserByUsername(registerDTO.login())).thenReturn(null);

        ResponseEntity<Void> response = authController.register(registerDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(authService, times(1)).register(registerDTO);
    }

    @Test
    void shouldReturnConflictWhenUserAlreadyExists() {
        RegisterDTO registerDTO = new RegisterDTO("existingUser", "securePassword", Roles.ADMIN);

        when(authService.loadUserByUsername(registerDTO.login())).thenReturn(mock(UserDetails.class));

        ResponseEntity<Void> response = authController.register(registerDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(authService, never()).register(registerDTO);
    }
}