package com.jcaa.appcrudusers.application.service;

import com.jcaa.appcrudusers.application.dto.command.DeleteVueloCommand;
import com.jcaa.appcrudusers.application.port.in.DeleteVueloUseCase;
import com.jcaa.appcrudusers.domain.port.out.DeleteVueloPort;
import com.jcaa.appcrudusers.domain.vo.VueloId;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class DeleteVueloService implements DeleteVueloUseCase {
    private final DeleteVueloPort deleteVueloPort;

    @Override
    public void delete(final DeleteVueloCommand command) {
        Objects.requireNonNull(command, "command");
        deleteVueloPort.delete(new VueloId(command.id()));
    }
}
