package com.jcaa.appcrudusers.application.port.in;

import com.jcaa.appcrudusers.application.dto.command.UpdateVueloCommand;
import com.jcaa.appcrudusers.application.dto.response.VueloResponseDto;

public interface UpdateVueloUseCase {
    VueloResponseDto update(String id, UpdateVueloCommand command);
}
