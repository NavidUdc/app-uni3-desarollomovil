package com.jcaa.appcrudusers.application.service;

import com.jcaa.appcrudusers.application.dto.query.ListUsersQuery;
import com.jcaa.appcrudusers.application.dto.response.UserResponseDto;
import com.jcaa.appcrudusers.application.mapper.ApplicationMapper;
import com.jcaa.appcrudusers.application.port.in.ListUsersUseCase;
import com.jcaa.appcrudusers.domain.model.User;
import com.jcaa.appcrudusers.domain.port.out.ListUsersPort;

import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ListUsersService implements ListUsersUseCase {
    private final ListUsersPort listUsersPort;

    @Override
    public List<UserResponseDto> list(final ListUsersQuery query) {
        Objects.requireNonNull(query, "query");
        List<User> users = listUsersPort.list();
        return ApplicationMapper.toResponse(users);
    }
}
