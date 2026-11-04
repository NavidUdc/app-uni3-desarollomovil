package com.jcaa.appcrudusers.entrypoint.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jcaa.appcrudusers.R;
import com.jcaa.appcrudusers.application.dto.response.AuthSessionResponseDto;
import com.jcaa.appcrudusers.application.port.in.GetSessionUseCase;
import com.jcaa.appcrudusers.entrypoint.util.AppExecutors;

import java.util.Optional;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
public class SplashActivity extends AppCompatActivity {

    @Inject
    GetSessionUseCase getSessionUseCase;

    @Inject
    AppExecutors appExecutors;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appExecutors.diskIO().execute(() -> {
            final Optional<AuthSessionResponseDto> sessionOpt = getSessionUseCase.getSession();
            appExecutors.mainThread().execute(() -> {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                if (sessionOpt.isPresent()) {
                    startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            });
        });
    }
}
