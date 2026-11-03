package com.jcaa.appcrudusers.application.service;

import com.jcaa.appcrudusers.application.dto.command.CreateUserCommand;
import com.jcaa.appcrudusers.application.dto.response.UserResponseDto;
import com.jcaa.appcrudusers.application.mapper.ApplicationMapper;
import com.jcaa.appcrudusers.application.port.in.CreateUserUseCase;
import com.jcaa.appcrudusers.domain.model.User;
import com.jcaa.appcrudusers.domain.model.UserRegistration;
import com.jcaa.appcrudusers.domain.port.out.CreateUserPort;

import java.util.Objects;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CreateUserService implements CreateUserUseCase {
    private final CreateUserPort createUserPort;

    @Override
    public UserResponseDto create(final CreateUserCommand command) {
        Objects.requireNonNull(command, "command");
        UserRegistration registration = ApplicationMapper.toDomain(command);
        User user = createUserPort.create(registration);
        return ApplicationMapper.toResponse(user);
    }
}
