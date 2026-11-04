package com.jcaa.appcrudusers.entrypoint;

import com.jcaa.appcrudusers.application.port.in.CreateUserUseCase;
import com.jcaa.appcrudusers.application.port.in.DeleteUserUseCase;
import com.jcaa.appcrudusers.application.port.in.GetSessionUseCase;
import com.jcaa.appcrudusers.application.port.in.GetUserByIdUseCase;
import com.jcaa.appcrudusers.application.port.in.ListUsersUseCase;
import com.jcaa.appcrudusers.application.port.in.LoginUseCase;
import com.jcaa.appcrudusers.application.port.in.LogoutUseCase;
import com.jcaa.appcrudusers.application.port.in.UpdateUserUseCase;
import com.jcaa.appcrudusers.domain.port.out.ClearSessionPort;
import com.jcaa.appcrudusers.domain.port.out.CreateUserPort;
import com.jcaa.appcrudusers.domain.port.out.DeleteUserPort;
import com.jcaa.appcrudusers.domain.port.out.GetUserByIdPort;
import com.jcaa.appcrudusers.domain.port.out.ListUsersPort;
import com.jcaa.appcrudusers.domain.port.out.LoadSessionPort;
import com.jcaa.appcrudusers.domain.port.out.LoginPort;
import com.jcaa.appcrudusers.domain.port.out.SaveSessionPort;
import com.jcaa.appcrudusers.domain.port.out.UpdateUserPort;
import com.jcaa.appcrudusers.entrypoint.di.AdapterModule;
import com.jcaa.appcrudusers.entrypoint.di.ExecutorsModule;
import com.jcaa.appcrudusers.entrypoint.di.UseCaseModule;
import com.jcaa.appcrudusers.entrypoint.util.AppExecutors;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class HiltModulesTest {

    @Test
    public void testExecutorsModuleProvidesNonNullExecutors() {
        final ExecutorsModule module = new ExecutorsModule();
        final AppExecutors executors = module.provideAppExecutors();
        assertNotNull(executors);
        assertNotNull(executors.diskIO());
        assertNotNull(executors.mainThread());
    }

    @Test
    public void testAdapterModuleInstantiation() {
        final AdapterModule module = new AdapterModule();
        assertNotNull(module);
    }

    @Test
    public void testUseCaseModuleProvidesUseCases() {
        final UseCaseModule module = new UseCaseModule();

        final LoginUseCase loginUseCase = module.provideLoginUseCase(mock(LoginPort.class), mock(SaveSessionPort.class));
        final GetSessionUseCase getSessionUseCase = module.provideGetSessionUseCase(mock(LoadSessionPort.class));
        final LogoutUseCase logoutUseCase = module.provideLogoutUseCase(mock(ClearSessionPort.class));
        final ListUsersUseCase listUsersUseCase = module.provideListUsersUseCase(mock(ListUsersPort.class));
        final GetUserByIdUseCase getUserByIdUseCase = module.provideGetUserByIdUseCase(mock(GetUserByIdPort.class));
        final CreateUserUseCase createUserUseCase = module.provideCreateUserUseCase(mock(CreateUserPort.class));
        final UpdateUserUseCase updateUserUseCase = module.provideUpdateUserUseCase(mock(UpdateUserPort.class));
        final DeleteUserUseCase deleteUserUseCase = module.provideDeleteUserUseCase(mock(DeleteUserPort.class));

        assertNotNull(loginUseCase);
        assertNotNull(getSessionUseCase);
        assertNotNull(logoutUseCase);
        assertNotNull(listUsersUseCase);
        assertNotNull(getUserByIdUseCase);
        assertNotNull(createUserUseCase);
        assertNotNull(updateUserUseCase);
        assertNotNull(deleteUserUseCase);
    }
}
