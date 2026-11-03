package com.jcaa.appcrudusers.application.dto.response;

import java.util.Objects;

public record UserResponseDto(
        String id,
        String name,
        String email,
        String role,
        String status
) {
    public UserResponseDto {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(email, "email");
        Objects.requireNonNull(role, "role");
        Objects.requireNonNull(status, "status");
    }
}
