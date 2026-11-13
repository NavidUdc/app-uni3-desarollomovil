package com.jcaa.appcrudusers.adapter.remote;

import com.jcaa.appcrudusers.adapter.http.ApiExecutor;
import com.jcaa.appcrudusers.adapter.http.SessionAuthorization;
import com.jcaa.appcrudusers.adapter.http.VuelosApi;
import com.jcaa.appcrudusers.adapter.mapper.VueloMapper;
import com.jcaa.appcrudusers.domain.model.Vuelo;
import com.jcaa.appcrudusers.domain.model.VueloUpdate;
import com.jcaa.appcrudusers.domain.port.out.UpdateVueloPort;
import com.jcaa.appcrudusers.domain.vo.VueloId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateVueloAdapter implements UpdateVueloPort {
    private final VuelosApi api;
    private final ApiExecutor executor;
    private final SessionAuthorization authorization;

    @Override
    public Vuelo update(final VueloId id, final VueloUpdate update) {
        return executor.execute(
                api.update(authorization.requireHeader(), id.getValue(), VueloMapper.toRequest(update)),
                VueloMapper::toDomain
        );
    }
}
