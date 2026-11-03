package com.jcaa.appcrudusers.application.dto.command;

import java.util.Objects;

public record CreateUserCommand(String id, String name, String email, String password, String role) {
    public CreateUserCommand {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(email, "email");
        Objects.requireNonNull(password, "password");
        Objects.requireNonNull(role, "role");
    }
}
