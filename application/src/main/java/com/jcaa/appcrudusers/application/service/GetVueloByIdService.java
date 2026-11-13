package com.jcaa.appcrudusers.application.service;

import com.jcaa.appcrudusers.application.dto.query.GetVueloByIdQuery;
import com.jcaa.appcrudusers.application.dto.response.VueloResponseDto;
import com.jcaa.appcrudusers.application.mapper.ApplicationMapper;
import com.jcaa.appcrudusers.application.port.in.GetVueloByIdUseCase;
import com.jcaa.appcrudusers.domain.model.Vuelo;
import com.jcaa.appcrudusers.domain.port.out.GetVueloByIdPort;
import com.jcaa.appcrudusers.domain.vo.VueloId;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class GetVueloByIdService implements GetVueloByIdUseCase {
    private final GetVueloByIdPort getVueloByIdPort;

    @Override
    public VueloResponseDto getById(final GetVueloByIdQuery query) {
        Objects.requireNonNull(query, "query");
        final Vuelo vuelo = getVueloByIdPort.getById(new VueloId(query.id()));
        return ApplicationMapper.toResponse(vuelo);
    }
}
