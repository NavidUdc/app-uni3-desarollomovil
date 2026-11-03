package com.jcaa.appcrudusers.application.service;

import com.jcaa.appcrudusers.application.dto.response.AuthSessionResponseDto;
import com.jcaa.appcrudusers.application.mapper.ApplicationMapper;
import com.jcaa.appcrudusers.application.port.in.GetSessionUseCase;
import com.jcaa.appcrudusers.domain.port.out.LoadSessionPort;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class GetSessionService implements GetSessionUseCase {
    private final LoadSessionPort loadSessionPort;

    @Override
    public Optional<AuthSessionResponseDto> getSession() {
        return loadSessionPort.load().map(ApplicationMapper::toResponse);
    }
}
