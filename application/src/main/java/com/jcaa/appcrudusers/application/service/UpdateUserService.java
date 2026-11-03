package com.jcaa.appcrudusers.application.service;

import com.jcaa.appcrudusers.application.dto.command.UpdateUserCommand;
import com.jcaa.appcrudusers.application.dto.response.UserResponseDto;
import com.jcaa.appcrudusers.application.mapper.ApplicationMapper;
import com.jcaa.appcrudusers.application.port.in.UpdateUserUseCase;
import com.jcaa.appcrudusers.domain.model.User;
import com.jcaa.appcrudusers.domain.model.UserUpdate;
import com.jcaa.appcrudusers.domain.port.out.UpdateUserPort;
import com.jcaa.appcrudusers.domain.vo.UserId;

import java.util.Objects;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateUserService implements UpdateUserUseCase {
    private final UpdateUserPort updateUserPort;

    @Override
    public UserResponseDto update(final UpdateUserCommand command) {
        Objects.requireNonNull(command, "command");
        UserId userId = new UserId(command.id());
        UserUpdate update = ApplicationMapper.toDomain(command);
        User user = updateUserPort.update(userId, update);
        return ApplicationMapper.toResponse(user);
    }
}
