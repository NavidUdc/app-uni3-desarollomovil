package com.jcaa.appcrudusers.application.port.in;

import com.jcaa.appcrudusers.application.dto.query.ListUsersQuery;
import com.jcaa.appcrudusers.application.dto.response.UserResponseDto;

import java.util.List;

public interface ListUsersUseCase {
    List<UserResponseDto> list(ListUsersQuery query);
}
