package com.jcaa.appcrudusers.application.port.in;

import com.jcaa.appcrudusers.application.dto.response.AuthSessionResponseDto;

import java.util.Optional;

public interface GetSessionUseCase {
    Optional<AuthSessionResponseDto> getSession();
}
