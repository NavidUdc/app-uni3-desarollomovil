package com.jcaa.appcrudusers.application.service;

import com.jcaa.appcrudusers.application.dto.command.UpdateVueloCommand;
import com.jcaa.appcrudusers.application.dto.response.VueloResponseDto;
import com.jcaa.appcrudusers.application.mapper.ApplicationMapper;
import com.jcaa.appcrudusers.application.port.in.UpdateVueloUseCase;
import com.jcaa.appcrudusers.domain.model.Vuelo;
import com.jcaa.appcrudusers.domain.model.VueloUpdate;
import com.jcaa.appcrudusers.domain.port.out.UpdateVueloPort;
import com.jcaa.appcrudusers.domain.vo.VueloId;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateVueloService implements UpdateVueloUseCase {
    private final UpdateVueloPort updateVueloPort;

    @Override
    public VueloResponseDto update(final String id, final UpdateVueloCommand command) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(command, "command");
        final VueloUpdate update = ApplicationMapper.toDomain(command);
        final Vuelo updated = updateVueloPort.update(new VueloId(id), update);
        return ApplicationMapper.toResponse(updated);
    }
}
