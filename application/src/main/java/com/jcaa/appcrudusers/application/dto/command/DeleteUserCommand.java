package com.jcaa.appcrudusers.application.dto.command;

import java.util.Objects;

public record DeleteUserCommand(String id) {
    public DeleteUserCommand {
        Objects.requireNonNull(id, "id");
    }
}
