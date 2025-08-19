package com.barberhq.api.user.service;

import com.barberhq.api.user.dto.RegisterRequest;
import com.barberhq.api.user.entity.User;
import com.barberhq.api.user.entity.UserRole;
import com.barberhq.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
}