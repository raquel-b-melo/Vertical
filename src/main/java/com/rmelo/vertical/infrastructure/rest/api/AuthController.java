package com.rmelo.vertical.infrastructure.rest.api;

import com.rmelo.vertical.application.service.AuthService;
import com.rmelo.vertical.application.service.TokenService;
import com.rmelo.vertical.core.domain.model.dto.AuthDTO;
import com.rmelo.vertical.core.domain.model.dto.LoginResponseDTO;
import com.rmelo.vertical.core.domain.model.dto.RegisterDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager,
                          AuthService authService, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthDTO authDTO){
        var usernamePassword = new UsernamePasswordAuthenticationToken(authDTO.login(),authDTO.password());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken(auth);
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO registerDTO){
        if (authService.loadUserByUsername(registerDTO.login()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        authService.register(registerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
