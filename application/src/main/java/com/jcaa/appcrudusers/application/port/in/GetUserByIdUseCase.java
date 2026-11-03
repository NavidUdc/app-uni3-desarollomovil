package com.jcaa.appcrudusers.application.port.in;

import com.jcaa.appcrudusers.application.dto.query.GetUserByIdQuery;
import com.jcaa.appcrudusers.application.dto.response.UserResponseDto;

public interface GetUserByIdUseCase {
    UserResponseDto getById(GetUserByIdQuery query);
}
