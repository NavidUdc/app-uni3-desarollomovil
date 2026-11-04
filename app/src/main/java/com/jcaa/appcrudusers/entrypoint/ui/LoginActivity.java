package com.jcaa.appcrudusers.entrypoint.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jcaa.appcrudusers.R;
import com.jcaa.appcrudusers.application.dto.command.LoginCommand;
import com.jcaa.appcrudusers.application.dto.response.AuthSessionResponseDto;
import com.jcaa.appcrudusers.application.port.in.LoginUseCase;
import com.jcaa.appcrudusers.entrypoint.util.AppExecutors;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    @Inject
    LoginUseCase loginUseCase;

    @Inject
    AppExecutors appExecutors;

    private EditText etEmail;
    private EditText etPassword;
    private TextView tvError;
    private ProgressBar progressBar;
    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvError = findViewById(R.id.tvError);
        progressBar = findViewById(R.id.progressBarLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> attemptLogin());
        btnRegister.setOnClickListener(v -> {
            final Intent intent = new Intent(LoginActivity.this, CreateUserActivity.class);
            intent.putExtra(CreateUserActivity.EXTRA_FROM_LOGIN, true);
            startActivity(intent);
        });
    }

    private void attemptLogin() {
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showError(getString(R.string.login_error_empty));
            return;
        }

        hideError();
        setLoading(true);

        appExecutors.diskIO().execute(() -> {
            try {
                final AuthSessionResponseDto session = loginUseCase.login(new LoginCommand(email, password));
                appExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
                    finish();
                });
            } catch (final Exception ex) {
                appExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    final String msg = Objects.nonNull(ex.getMessage()) ? ex.getMessage() : getString(R.string.error);
                    showError(msg);
                });
            }
        });
    }

    private void setLoading(final boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!loading);
        btnRegister.setEnabled(!loading);
        etEmail.setEnabled(!loading);
        etPassword.setEnabled(!loading);
    }

    private void showError(final String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        tvError.setVisibility(View.GONE);
    }
}
