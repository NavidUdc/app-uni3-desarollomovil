package com.jcaa.appcrudusers.entrypoint.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.jcaa.appcrudusers.R;
import com.jcaa.appcrudusers.application.dto.response.AuthSessionResponseDto;
import com.jcaa.appcrudusers.application.port.in.GetSessionUseCase;
import com.jcaa.appcrudusers.application.port.in.LogoutUseCase;
import com.jcaa.appcrudusers.entrypoint.util.AppExecutors;

import java.util.Optional;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WelcomeActivity extends BaseDrawerActivity {

    @Inject
    GetSessionUseCase getSessionUseCase;

    @Inject
    LogoutUseCase welcomeLogoutUseCase;

    @Inject
    AppExecutors welcomeAppExecutors;

    private TextView tvSessionEmail;
    private TextView tvSessionToken;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDrawerContentView(R.layout.activity_welcome);
        setDrawerTitle(getString(R.string.welcome_title));
        setSelectedMenuItem(R.id.nav_home);

        tvSessionEmail = findViewById(R.id.tvSessionEmail);
        tvSessionToken = findViewById(R.id.tvSessionToken);
        final Button btnUserManagement = findViewById(R.id.btnUserManagement);
        final Button btnVuelosManagement = findViewById(R.id.btnVuelosManagement);
        final Button btnLogout = findViewById(R.id.btnLogout);

        btnUserManagement.setOnClickListener(v -> startActivity(new Intent(WelcomeActivity.this, UserListActivity.class)));
        btnVuelosManagement.setOnClickListener(v -> startActivity(new Intent(WelcomeActivity.this, VueloListActivity.class)));
        btnLogout.setOnClickListener(v -> confirmLogout());

        loadSessionData();
    }

    private void loadSessionData() {
        welcomeAppExecutors.diskIO().execute(() -> {
            final Optional<AuthSessionResponseDto> sessionOpt = getSessionUseCase.getSession();
            welcomeAppExecutors.mainThread().execute(() -> {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                if (sessionOpt.isPresent()) {
                    final AuthSessionResponseDto session = sessionOpt.get();
                    tvSessionEmail.setText(getString(R.string.welcome_subtitle));
                    final String token = session.accessToken();
                    final String maskedToken = token != null && token.length() > 20
                            ? token.substring(0, 10) + "..." + token.substring(token.length() - 10)
                            : token;
                    tvSessionToken.setText(maskedToken);
                } else {
                    navigateToLogin();
                }
            });
        });
    }

    private void confirmLogout() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.btn_logout)
                .setMessage(R.string.logout_confirm)
                .setPositiveButton(R.string.confirm, (dialog, which) -> executeLogout())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void executeLogout() {
        welcomeAppExecutors.diskIO().execute(() -> {
            welcomeLogoutUseCase.logout();
            welcomeAppExecutors.mainThread().execute(() -> {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                Toast.makeText(WelcomeActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                navigateToLogin();
            });
        });
    }

    private void navigateToLogin() {
        final Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
