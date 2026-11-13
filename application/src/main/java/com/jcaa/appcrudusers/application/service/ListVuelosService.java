package com.jcaa.appcrudusers.application.service;

import com.jcaa.appcrudusers.application.dto.query.ListVuelosQuery;
import com.jcaa.appcrudusers.application.dto.response.VueloResponseDto;
import com.jcaa.appcrudusers.application.mapper.ApplicationMapper;
import com.jcaa.appcrudusers.application.port.in.ListVuelosUseCase;
import com.jcaa.appcrudusers.domain.model.Vuelo;
import com.jcaa.appcrudusers.domain.port.out.ListVuelosPort;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ListVuelosService implements ListVuelosUseCase {
    private final ListVuelosPort listVuelosPort;

    @Override
    public List<VueloResponseDto> list(final ListVuelosQuery query) {
        Objects.requireNonNull(query, "query");
        final List<Vuelo> vuelos = listVuelosPort.list();
        return ApplicationMapper.toVueloResponseList(vuelos);
    }
}
