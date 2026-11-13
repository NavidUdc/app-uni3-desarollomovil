package com.jcaa.appcrudusers.application.port.in;

import com.jcaa.appcrudusers.application.dto.command.CreateVueloCommand;
import com.jcaa.appcrudusers.application.dto.response.VueloResponseDto;

public interface CreateVueloUseCase {
    VueloResponseDto create(CreateVueloCommand command);
}
