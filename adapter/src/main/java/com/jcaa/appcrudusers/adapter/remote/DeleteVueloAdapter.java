package com.jcaa.appcrudusers.adapter.remote;

import com.jcaa.appcrudusers.adapter.http.ApiExecutor;
import com.jcaa.appcrudusers.adapter.http.SessionAuthorization;
import com.jcaa.appcrudusers.adapter.http.VuelosApi;
import com.jcaa.appcrudusers.domain.port.out.DeleteVueloPort;
import com.jcaa.appcrudusers.domain.vo.VueloId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class DeleteVueloAdapter implements DeleteVueloPort {
    private final VuelosApi api;
    private final ApiExecutor executor;
    private final SessionAuthorization authorization;

    @Override
    public void delete(final VueloId id) {
        executor.executeDelete(api.delete(authorization.requireHeader(), id.getValue()));
    }
}
