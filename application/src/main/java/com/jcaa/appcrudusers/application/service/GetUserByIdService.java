package com.jcaa.appcrudusers.application.service;

import com.jcaa.appcrudusers.application.dto.query.GetUserByIdQuery;
import com.jcaa.appcrudusers.application.dto.response.UserResponseDto;
import com.jcaa.appcrudusers.application.mapper.ApplicationMapper;
import com.jcaa.appcrudusers.application.port.in.GetUserByIdUseCase;
import com.jcaa.appcrudusers.domain.model.User;
import com.jcaa.appcrudusers.domain.port.out.GetUserByIdPort;
import com.jcaa.appcrudusers.domain.vo.UserId;

import java.util.Objects;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class GetUserByIdService implements GetUserByIdUseCase {
    private final GetUserByIdPort getUserByIdPort;

    @Override
    public UserResponseDto getById(final GetUserByIdQuery query) {
        Objects.requireNonNull(query, "query");
        UserId userId = new UserId(query.id());
        User user = getUserByIdPort.getById(userId);
        return ApplicationMapper.toResponse(user);
    }
}
