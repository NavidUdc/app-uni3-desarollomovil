package com.jcaa.appcrudusers.application.port.in;

import com.jcaa.appcrudusers.application.dto.command.DeleteUserCommand;

public interface DeleteUserUseCase {
    void delete(DeleteUserCommand command);
}
