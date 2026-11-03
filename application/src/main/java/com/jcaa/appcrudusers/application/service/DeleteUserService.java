package com.jcaa.appcrudusers.application.service;

import com.jcaa.appcrudusers.application.dto.command.DeleteUserCommand;
import com.jcaa.appcrudusers.application.port.in.DeleteUserUseCase;
import com.jcaa.appcrudusers.domain.port.out.DeleteUserPort;
import com.jcaa.appcrudusers.domain.vo.UserId;

import java.util.Objects;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class DeleteUserService implements DeleteUserUseCase {
    private final DeleteUserPort deleteUserPort;

    @Override
    public void delete(final DeleteUserCommand command) {
        Objects.requireNonNull(command, "command");
        UserId userId = new UserId(command.id());
        deleteUserPort.delete(userId);
    }
}
