package com.jcaa.appcrudusers.entrypoint.di;

import com.jcaa.appcrudusers.application.port.in.CreateUserUseCase;
import com.jcaa.appcrudusers.application.port.in.CreateVueloUseCase;
import com.jcaa.appcrudusers.application.port.in.DeleteUserUseCase;
import com.jcaa.appcrudusers.application.port.in.DeleteVueloUseCase;
import com.jcaa.appcrudusers.application.port.in.GetSessionUseCase;
import com.jcaa.appcrudusers.application.port.in.GetUserByIdUseCase;
import com.jcaa.appcrudusers.application.port.in.GetVueloByIdUseCase;
import com.jcaa.appcrudusers.application.port.in.ListUsersUseCase;
import com.jcaa.appcrudusers.application.port.in.ListVuelosUseCase;
import com.jcaa.appcrudusers.application.port.in.LoginUseCase;
import com.jcaa.appcrudusers.application.port.in.LogoutUseCase;
import com.jcaa.appcrudusers.application.port.in.UpdateUserUseCase;
import com.jcaa.appcrudusers.application.port.in.UpdateVueloUseCase;
import com.jcaa.appcrudusers.application.service.CreateUserService;
import com.jcaa.appcrudusers.application.service.CreateVueloService;
import com.jcaa.appcrudusers.application.service.DeleteUserService;
import com.jcaa.appcrudusers.application.service.DeleteVueloService;
import com.jcaa.appcrudusers.application.service.GetSessionService;
import com.jcaa.appcrudusers.application.service.GetUserByIdService;
import com.jcaa.appcrudusers.application.service.GetVueloByIdService;
import com.jcaa.appcrudusers.application.service.ListUsersService;
import com.jcaa.appcrudusers.application.service.ListVuelosService;
import com.jcaa.appcrudusers.application.service.LoginService;
import com.jcaa.appcrudusers.application.service.LogoutService;
import com.jcaa.appcrudusers.application.service.UpdateUserService;
import com.jcaa.appcrudusers.application.service.UpdateVueloService;
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

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public final class UseCaseModule {

    @Provides
    public LoginUseCase provideLoginUseCase(
            final LoginPort loginPort,
            final SaveSessionPort saveSessionPort
    ) {
        return new LoginService(loginPort, saveSessionPort);
    }

    @Provides
    public GetSessionUseCase provideGetSessionUseCase(final LoadSessionPort loadSessionPort) {
        return new GetSessionService(loadSessionPort);
    }

    @Provides
    public LogoutUseCase provideLogoutUseCase(final ClearSessionPort clearSessionPort) {
        return new LogoutService(clearSessionPort);
    }

    @Provides
    public ListUsersUseCase provideListUsersUseCase(final ListUsersPort listUsersPort) {
        return new ListUsersService(listUsersPort);
    }

    @Provides
    public GetUserByIdUseCase provideGetUserByIdUseCase(final GetUserByIdPort getUserByIdPort) {
        return new GetUserByIdService(getUserByIdPort);
    }

    @Provides
    public CreateUserUseCase provideCreateUserUseCase(final CreateUserPort createUserPort) {
        return new CreateUserService(createUserPort);
    }

    @Provides
    public UpdateUserUseCase provideUpdateUserUseCase(final UpdateUserPort updateUserPort) {
        return new UpdateUserService(updateUserPort);
    }

    @Provides
    public DeleteUserUseCase provideDeleteUserUseCase(final DeleteUserPort deleteUserPort) {
        return new DeleteUserService(deleteUserPort);
    }

    @Provides
    public CreateVueloUseCase provideCreateVueloUseCase(final CreateVueloPort createVueloPort) {
        return new CreateVueloService(createVueloPort);
    }

    @Provides
    public GetVueloByIdUseCase provideGetVueloByIdUseCase(final GetVueloByIdPort getVueloByIdPort) {
        return new GetVueloByIdService(getVueloByIdPort);
    }

    @Provides
    public ListVuelosUseCase provideListVuelosUseCase(final ListVuelosPort listVuelosPort) {
        return new ListVuelosService(listVuelosPort);
    }

    @Provides
    public UpdateVueloUseCase provideUpdateVueloUseCase(final UpdateVueloPort updateVueloPort) {
        return new UpdateVueloService(updateVueloPort);
    }

    @Provides
    public DeleteVueloUseCase provideDeleteVueloUseCase(final DeleteVueloPort deleteVueloPort) {
        return new DeleteVueloService(deleteVueloPort);
    }
}
