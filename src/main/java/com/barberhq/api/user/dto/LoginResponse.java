package com.barberhq.api.user.dto;

import com.barberhq.api.user.entity.UserRole;
import java.util.UUID;

public record LoginResponse(
        UUID userId,
        String name,
        String email,
        UserRole role,
        String token,
        String tokenType
) {
    public LoginResponse(UUID userId, String name, String email, UserRole role, String token) {
        this(userId, name, email, role, token, "Bearer");
    }
}