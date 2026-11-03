package com.jcaa.appcrudusers.application.port.in;

import com.jcaa.appcrudusers.application.dto.command.UpdateUserCommand;
import com.jcaa.appcrudusers.application.dto.response.UserResponseDto;

public interface UpdateUserUseCase {
    UserResponseDto update(UpdateUserCommand command);
}
