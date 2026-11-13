package com.jcaa.appcrudusers.adapter.remote;

import com.jcaa.appcrudusers.adapter.http.ApiExecutor;
import com.jcaa.appcrudusers.adapter.http.SessionAuthorization;
import com.jcaa.appcrudusers.adapter.http.VuelosApi;
import com.jcaa.appcrudusers.adapter.mapper.VueloMapper;
import com.jcaa.appcrudusers.domain.model.Vuelo;
import com.jcaa.appcrudusers.domain.port.out.ListVuelosPort;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ListVuelosAdapter implements ListVuelosPort {
    private final VuelosApi api;
    private final ApiExecutor executor;
    private final SessionAuthorization authorization;

    @Override
    public List<Vuelo> list() {
        return executor.execute(api.list(authorization.requireHeader()), VueloMapper::toDomain);
    }
}
