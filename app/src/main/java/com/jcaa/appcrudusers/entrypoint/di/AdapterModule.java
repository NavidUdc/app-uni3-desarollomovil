package com.jcaa.appcrudusers.entrypoint.di;

import android.content.Context;

import com.jcaa.appcrudusers.adapter.config.AdapterFactory;
import com.jcaa.appcrudusers.domain.port.out.ClearSessionPort;
import com.jcaa.appcrudusers.domain.port.out.CreateUserPort;
import com.jcaa.appcrudusers.domain.port.out.DeleteUserPort;
import com.jcaa.appcrudusers.domain.port.out.GetUserByIdPort;
import com.jcaa.appcrudusers.domain.port.out.ListUsersPort;
import com.jcaa.appcrudusers.domain.port.out.LoadSessionPort;
import com.jcaa.appcrudusers.domain.port.out.LoginPort;
import com.jcaa.appcrudusers.domain.port.out.SaveSessionPort;
import com.jcaa.appcrudusers.domain.port.out.UpdateUserPort;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public final class AdapterModule {

    @Provides
    @Singleton
    public AdapterFactory provideAdapterFactory(@ApplicationContext final Context context) {
        return new AdapterFactory(context);
    }

    @Provides
    public LoginPort provideLoginPort(final AdapterFactory adapterFactory) {
        return adapterFactory.getLogin();
    }

    @Provides
    public SaveSessionPort provideSaveSessionPort(final AdapterFactory adapterFactory) {
        return adapterFactory.getSaveSession();
    }

    @Provides
    public LoadSessionPort provideLoadSessionPort(final AdapterFactory adapterFactory) {
        return adapterFactory.getLoadSession();
    }

    @Provides
    public ClearSessionPort provideClearSessionPort(final AdapterFactory adapterFactory) {
        return adapterFactory.getClearSession();
    }

    @Provides
    public ListUsersPort provideListUsersPort(final AdapterFactory adapterFactory) {
        return adapterFactory.getListUsers();
    }

    @Provides
    public GetUserByIdPort provideGetUserByIdPort(final AdapterFactory adapterFactory) {
        return adapterFactory.getGetUserById();
    }

    @Provides
    public CreateUserPort provideCreateUserPort(final AdapterFactory adapterFactory) {
        return adapterFactory.getCreateUser();
    }

    @Provides
    public UpdateUserPort provideUpdateUserPort(final AdapterFactory adapterFactory) {
        return adapterFactory.getUpdateUser();
    }

    @Provides
    public DeleteUserPort provideDeleteUserPort(final AdapterFactory adapterFactory) {
        return adapterFactory.getDeleteUser();
    }
}
