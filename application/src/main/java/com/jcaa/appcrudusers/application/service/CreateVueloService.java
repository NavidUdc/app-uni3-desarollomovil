package com.jcaa.appcrudusers.application.service;

import com.jcaa.appcrudusers.application.dto.command.CreateVueloCommand;
import com.jcaa.appcrudusers.application.dto.response.VueloResponseDto;
import com.jcaa.appcrudusers.application.mapper.ApplicationMapper;
import com.jcaa.appcrudusers.application.port.in.CreateVueloUseCase;
import com.jcaa.appcrudusers.domain.model.Vuelo;
import com.jcaa.appcrudusers.domain.model.VueloRegistration;
import com.jcaa.appcrudusers.domain.port.out.CreateVueloPort;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CreateVueloService implements CreateVueloUseCase {
    private final CreateVueloPort createVueloPort;

    @Override
    public VueloResponseDto create(final CreateVueloCommand command) {
        Objects.requireNonNull(command, "command");
        final VueloRegistration registration = ApplicationMapper.toDomain(command);
        final Vuelo vuelo = createVueloPort.create(registration);
        return ApplicationMapper.toResponse(vuelo);
    }
}
