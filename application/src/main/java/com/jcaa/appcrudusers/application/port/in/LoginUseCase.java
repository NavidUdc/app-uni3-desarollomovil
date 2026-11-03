package com.jcaa.appcrudusers.application.port.in;

import com.jcaa.appcrudusers.application.dto.command.LoginCommand;
import com.jcaa.appcrudusers.application.dto.response.AuthSessionResponseDto;

public interface LoginUseCase {
    AuthSessionResponseDto login(LoginCommand command);
}
