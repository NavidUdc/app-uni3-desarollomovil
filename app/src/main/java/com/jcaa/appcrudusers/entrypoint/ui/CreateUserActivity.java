package com.jcaa.appcrudusers.entrypoint.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jcaa.appcrudusers.R;
import com.jcaa.appcrudusers.application.dto.command.CreateUserCommand;
import com.jcaa.appcrudusers.application.port.in.CreateUserUseCase;
import com.jcaa.appcrudusers.entrypoint.util.AppExecutors;

import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateUserActivity extends BaseDrawerActivity {

    public static final String EXTRA_FROM_LOGIN = "extra_from_login";

    private static final String[] ROLES = new String[]{"USER", "ADMIN"};

    @Inject
    CreateUserUseCase createUserUseCase;

    @Inject
    AppExecutors createUserAppExecutors;

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private Spinner spRole;
    private TextView tvError;
    private ProgressBar progressBar;
    private Button btnSave;
    private boolean fromLogin;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDrawerContentView(R.layout.activity_create_user);

        fromLogin = getIntent().getBooleanExtra(EXTRA_FROM_LOGIN, false);
        setDrawerTitle(getString(R.string.create_user_title));

        if (fromLogin) {
            enableUpNavigation();
        } else {
            setSelectedMenuItem(R.id.nav_create_user);
        }

        etName = findViewById(R.id.etCreateName);
        etEmail = findViewById(R.id.etCreateEmail);
        etPassword = findViewById(R.id.etCreatePassword);
        spRole = findViewById(R.id.spCreateRole);
        tvError = findViewById(R.id.tvCreateError);
        progressBar = findViewById(R.id.progressBarCreate);
        btnSave = findViewById(R.id.btnSaveCreate);

        final ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                ROLES
        );
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRole.setAdapter(roleAdapter);

        btnSave.setOnClickListener(v -> saveUser());
    }

    private void saveUser() {
        final String name = etName.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String role = spRole.getSelectedItem() != null ? spRole.getSelectedItem().toString() : "USER";

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("Todos los campos son obligatorios");
            return;
        }

        hideError();
        setLoading(true);

        createUserAppExecutors.diskIO().execute(() -> {
            try {
                final String generatedId = UUID.randomUUID().toString();
                createUserUseCase.create(new CreateUserCommand(generatedId, name, email, password, role));
                createUserAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    Toast.makeText(CreateUserActivity.this, R.string.user_created_success, Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (final Exception ex) {
                createUserAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    final String msg = Objects.nonNull(ex.getMessage()) ? ex.getMessage() : getString(R.string.error);
                    if (fromLogin) {
                        showError(msg);
                    } else {
                        handleOperationError(ex, () -> showError(msg));
                    }
                });
            }
        });
    }

    private void setLoading(final boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnSave.setEnabled(!loading);
    }

    private void showError(final String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        tvError.setVisibility(View.GONE);
    }
}
