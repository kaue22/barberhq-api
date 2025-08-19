package com.barberhq.api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Usando 'record' para um DTO imutável e conciso
public record RegisterRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres") String password
) {}