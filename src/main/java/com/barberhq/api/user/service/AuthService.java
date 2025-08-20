package com.barberhq.api.user.service;

import com.barberhq.api.config.security.JwtService;
import com.barberhq.api.user.dto.LoginRequest;
import com.barberhq.api.user.dto.LoginResponse;
import com.barberhq.api.user.dto.RegisterRequest;
import com.barberhq.api.user.entity.User;
import com.barberhq.api.user.entity.UserRole;
import com.barberhq.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public User register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalStateException("Email já está em uso.");
        }
        String hashedPassword = passwordEncoder.encode(request.password());
        User newUser = new User(
                null,
                request.name(),
                request.email(),
                hashedPassword,
                UserRole.ADMIN
        );

        return userRepository.save(newUser);
    }

    public LoginResponse login(LoginRequest request) {
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );

        return new LoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                token
        );
    }
}