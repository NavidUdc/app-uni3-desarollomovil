package com.jcaa.appcrudusers.application.dto.response;

import java.time.Instant;
import java.util.Objects;

public record AuthSessionResponseDto(
        String accessToken,
        String tokenType,
        long expiresIn,
        Instant receivedAt
) {
    public AuthSessionResponseDto {
        Objects.requireNonNull(accessToken, "accessToken");
        Objects.requireNonNull(tokenType, "tokenType");
        Objects.requireNonNull(receivedAt, "receivedAt");
    }
}
