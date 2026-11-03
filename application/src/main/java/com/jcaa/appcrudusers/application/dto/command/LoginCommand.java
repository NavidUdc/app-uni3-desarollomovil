package com.jcaa.appcrudusers.application.dto.command;

import java.util.Objects;

public record LoginCommand(String email, String password) {
    public LoginCommand {
        Objects.requireNonNull(email, "email");
        Objects.requireNonNull(password, "password");
    }
}
