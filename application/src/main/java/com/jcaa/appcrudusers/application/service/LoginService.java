package com.jcaa.appcrudusers.application.service;

import com.jcaa.appcrudusers.application.dto.command.LoginCommand;
import com.jcaa.appcrudusers.application.dto.response.AuthSessionResponseDto;
import com.jcaa.appcrudusers.application.mapper.ApplicationMapper;
import com.jcaa.appcrudusers.application.port.in.LoginUseCase;
import com.jcaa.appcrudusers.domain.model.AuthSession;
import com.jcaa.appcrudusers.domain.model.Credentials;
import com.jcaa.appcrudusers.domain.port.out.LoginPort;
import com.jcaa.appcrudusers.domain.port.out.SaveSessionPort;

import java.util.Objects;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class LoginService implements LoginUseCase {
    private final LoginPort loginPort;
    private final SaveSessionPort saveSessionPort;

    @Override
    public AuthSessionResponseDto login(final LoginCommand command) {
        Objects.requireNonNull(command, "command");
        Credentials credentials = ApplicationMapper.toDomain(command);
        AuthSession session = loginPort.login(credentials);
        saveSessionPort.save(session);
        return ApplicationMapper.toResponse(session);
    }
}
