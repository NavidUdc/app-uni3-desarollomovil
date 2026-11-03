package com.jcaa.appcrudusers.application.port.in;

import com.jcaa.appcrudusers.application.dto.command.CreateUserCommand;
import com.jcaa.appcrudusers.application.dto.response.UserResponseDto;

public interface CreateUserUseCase {
    UserResponseDto create(CreateUserCommand command);
}
