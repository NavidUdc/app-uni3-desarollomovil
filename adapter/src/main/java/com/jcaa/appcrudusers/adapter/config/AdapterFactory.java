package com.jcaa.appcrudusers.adapter.config;

import android.content.Context;
import com.google.gson.Gson;
import com.jcaa.appcrudusers.adapter.cache.AesGcmSessionCipher;
import com.jcaa.appcrudusers.adapter.cache.AndroidKeystoreKeyProvider;
import com.jcaa.appcrudusers.adapter.cache.AtomicFileSessionBlobStore;
import com.jcaa.appcrudusers.adapter.cache.EncryptedSessionStore;
import com.jcaa.appcrudusers.adapter.http.ApiErrorMapper;
import com.jcaa.appcrudusers.adapter.http.ApiExecutor;
import com.jcaa.appcrudusers.adapter.http.SessionAuthorization;
import com.jcaa.appcrudusers.adapter.http.UsersApi;
import com.jcaa.appcrudusers.adapter.http.VuelosApi;
import com.jcaa.appcrudusers.adapter.local.ClearSessionAdapter;
import com.jcaa.appcrudusers.adapter.local.LoadSessionAdapter;
import com.jcaa.appcrudusers.adapter.local.SaveSessionAdapter;
import com.jcaa.appcrudusers.adapter.remote.CreateUserAdapter;
import com.jcaa.appcrudusers.adapter.remote.CreateVueloAdapter;
import com.jcaa.appcrudusers.adapter.remote.DeleteUserAdapter;
import com.jcaa.appcrudusers.adapter.remote.DeleteVueloAdapter;
import com.jcaa.appcrudusers.adapter.remote.GetUserByIdAdapter;
import com.jcaa.appcrudusers.adapter.remote.GetVueloByIdAdapter;
import com.jcaa.appcrudusers.adapter.remote.ListUsersAdapter;
import com.jcaa.appcrudusers.adapter.remote.ListVuelosAdapter;
import com.jcaa.appcrudusers.adapter.remote.LoginAdapter;
import com.jcaa.appcrudusers.adapter.remote.UpdateUserAdapter;
import com.jcaa.appcrudusers.adapter.remote.UpdateVueloAdapter;
import com.jcaa.appcrudusers.domain.port.out.ClearSessionPort;
import com.jcaa.appcrudusers.domain.port.out.CreateUserPort;
import com.jcaa.appcrudusers.domain.port.out.CreateVueloPort;
import com.jcaa.appcrudusers.domain.port.out.DeleteUserPort;
import com.jcaa.appcrudusers.domain.port.out.DeleteVueloPort;
import com.jcaa.appcrudusers.domain.port.out.GetUserByIdPort;
import com.jcaa.appcrudusers.domain.port.out.GetVueloByIdPort;
import com.jcaa.appcrudusers.domain.port.out.ListUsersPort;
import com.jcaa.appcrudusers.domain.port.out.ListVuelosPort;
import com.jcaa.appcrudusers.domain.port.out.LoadSessionPort;
import com.jcaa.appcrudusers.domain.port.out.LoginPort;
import com.jcaa.appcrudusers.domain.port.out.SaveSessionPort;
import com.jcaa.appcrudusers.domain.port.out.UpdateUserPort;
import com.jcaa.appcrudusers.domain.port.out.UpdateVueloPort;
import java.time.Clock;
import lombok.Getter;

/** Create once with application context and share across application services. No hidden global state. */
@Getter
public final class AdapterFactory {
    private final LoginPort login;
    private final CreateUserPort createUser;
    private final GetUserByIdPort getUserById;
    private final ListUsersPort listUsers;
    private final UpdateUserPort updateUser;
    private final DeleteUserPort deleteUser;
    private final SaveSessionPort saveSession;
    private final LoadSessionPort loadSession;
    private final ClearSessionPort clearSession;
    private final CreateVueloPort createVuelo;
    private final GetVueloByIdPort getVueloById;
    private final ListVuelosPort listVuelos;
    private final UpdateVueloPort updateVuelo;
    private final DeleteVueloPort deleteVuelo;

    public AdapterFactory(final Context context) {
        final Gson gson = new Gson();
        final Clock clock = Clock.systemUTC();
        final EncryptedSessionStore store = new EncryptedSessionStore(
                new AtomicFileSessionBlobStore(context),
                new AesGcmSessionCipher(new AndroidKeystoreKeyProvider()),
                gson);
        saveSession = new SaveSessionAdapter(store);
        loadSession = new LoadSessionAdapter(store);
        clearSession = new ClearSessionAdapter(store);
        final UsersApi api = HttpClientFactory.createApi(gson);
        final VuelosApi vuelosApi = HttpClientFactory.createVuelosApi(gson);
        final ApiExecutor executor = new ApiExecutor(new ApiErrorMapper(gson));
        final SessionAuthorization authorization = new SessionAuthorization(loadSession, clock);
        login = new LoginAdapter(api, executor, clock);
        createUser = new CreateUserAdapter(api, executor);
        getUserById = new GetUserByIdAdapter(api, executor, authorization);
        listUsers = new ListUsersAdapter(api, executor, authorization);
        updateUser = new UpdateUserAdapter(api, executor, authorization);
        deleteUser = new DeleteUserAdapter(api, executor, authorization);
        createVuelo = new CreateVueloAdapter(vuelosApi, executor, authorization);
        getVueloById = new GetVueloByIdAdapter(vuelosApi, executor, authorization);
        listVuelos = new ListVuelosAdapter(vuelosApi, executor, authorization);
        updateVuelo = new UpdateVueloAdapter(vuelosApi, executor, authorization);
        deleteVuelo = new DeleteVueloAdapter(vuelosApi, executor, authorization);
    }
}
