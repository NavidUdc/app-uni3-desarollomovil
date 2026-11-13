package com.jcaa.appcrudusers.adapter.remote;

import com.jcaa.appcrudusers.adapter.http.ApiExecutor;
import com.jcaa.appcrudusers.adapter.http.SessionAuthorization;
import com.jcaa.appcrudusers.adapter.http.VuelosApi;
import com.jcaa.appcrudusers.adapter.mapper.VueloMapper;
import com.jcaa.appcrudusers.domain.model.Vuelo;
import com.jcaa.appcrudusers.domain.port.out.GetVueloByIdPort;
import com.jcaa.appcrudusers.domain.vo.VueloId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class GetVueloByIdAdapter implements GetVueloByIdPort {
    private final VuelosApi api;
    private final ApiExecutor executor;
    private final SessionAuthorization authorization;

    @Override
    public Vuelo getById(final VueloId id) {
        return executor.execute(
                api.getById(authorization.requireHeader(), id.getValue()),
                VueloMapper::toDomain
        );
    }
}
