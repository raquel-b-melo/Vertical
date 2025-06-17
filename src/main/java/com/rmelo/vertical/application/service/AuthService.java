package com.rmelo.vertical.application.service;

import com.rmelo.vertical.core.domain.model.Logons;
import com.rmelo.vertical.core.domain.model.dto.RegisterDTO;
import com.rmelo.vertical.core.domain.repository.LogonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final LogonRepository logonRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(LogonRepository logonRepository, PasswordEncoder passwordEncoder) {
        this.logonRepository = logonRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return logonRepository.findByLogin(username);
    }

    public void register(RegisterDTO registerDTO){
        String encryptedPassword = passwordEncoder.encode(registerDTO.password());
        Logons logon = new Logons(registerDTO.login(), encryptedPassword, registerDTO.role());
        logonRepository.save(logon);
    }
}
