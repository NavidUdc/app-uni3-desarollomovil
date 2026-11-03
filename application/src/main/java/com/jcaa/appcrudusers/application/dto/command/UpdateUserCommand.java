package com.jcaa.appcrudusers.application.dto.command;

import java.util.Objects;
import java.util.Optional;

public record UpdateUserCommand(
        String id,
        String name,
        String email,
        String password,
        String role,
        String status
) {
    public UpdateUserCommand {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(email, "email");
        Objects.requireNonNull(role, "role");
        Objects.requireNonNull(status, "status");
    }

    public Optional<String> passwordOpt() {
        return Optional.ofNullable(password);
    }
}
