package com.jcaa.appcrudusers.application.service;

import com.jcaa.appcrudusers.application.port.in.LogoutUseCase;
import com.jcaa.appcrudusers.domain.port.out.ClearSessionPort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class LogoutService implements LogoutUseCase {
    private final ClearSessionPort clearSessionPort;

    @Override
    public void logout() {
        clearSessionPort.clear();
    }
}
