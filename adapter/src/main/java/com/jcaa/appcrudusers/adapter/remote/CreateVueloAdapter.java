package com.jcaa.appcrudusers.adapter.remote;

import com.jcaa.appcrudusers.adapter.http.ApiExecutor;
import com.jcaa.appcrudusers.adapter.http.SessionAuthorization;
import com.jcaa.appcrudusers.adapter.http.VuelosApi;
import com.jcaa.appcrudusers.adapter.mapper.VueloMapper;
import com.jcaa.appcrudusers.domain.model.Vuelo;
import com.jcaa.appcrudusers.domain.model.VueloRegistration;
import com.jcaa.appcrudusers.domain.port.out.CreateVueloPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CreateVueloAdapter implements CreateVueloPort {
    private final VuelosApi api;
    private final ApiExecutor executor;
    private final SessionAuthorization authorization;

    @Override
    public Vuelo create(final VueloRegistration registration) {
        return executor.execute(
                api.create(authorization.requireHeader(), VueloMapper.toRequest(registration)),
                VueloMapper::toDomain
        );
    }
}
