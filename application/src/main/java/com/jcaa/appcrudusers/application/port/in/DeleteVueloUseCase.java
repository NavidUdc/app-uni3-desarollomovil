package com.jcaa.appcrudusers.application.port.in;

import com.jcaa.appcrudusers.application.dto.command.DeleteVueloCommand;

public interface DeleteVueloUseCase {
    void delete(DeleteVueloCommand command);
}
