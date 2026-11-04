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
import com.jcaa.appcrudusers.application.dto.command.UpdateUserCommand;
import com.jcaa.appcrudusers.application.dto.query.GetUserByIdQuery;
import com.jcaa.appcrudusers.application.dto.response.UserResponseDto;
import com.jcaa.appcrudusers.application.port.in.GetUserByIdUseCase;
import com.jcaa.appcrudusers.application.port.in.UpdateUserUseCase;
import com.jcaa.appcrudusers.entrypoint.util.AppExecutors;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UpdateUserActivity extends BaseDrawerActivity {

    private static final String[] ROLES = new String[]{"USER", "ADMIN"};
    private static final String[] STATUSES = new String[]{"ACTIVE", "INACTIVE", "SUSPENDED"};

    @Inject
    UpdateUserUseCase updateUserUseCase;

    @Inject
    GetUserByIdUseCase getUserByIdUseCase;

    @Inject
    AppExecutors updateUserAppExecutors;

    private String userId;

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private Spinner spRole;
    private Spinner spStatus;
    private TextView tvError;
    private ProgressBar progressBar;
    private Button btnSave;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDrawerContentView(R.layout.activity_update_user);
        setDrawerTitle(getString(R.string.edit_user_title));
        enableUpNavigation();

        userId = getIntent().getStringExtra(UserListActivity.EXTRA_USER_ID);

        etName = findViewById(R.id.etUpdateName);
        etEmail = findViewById(R.id.etUpdateEmail);
        etPassword = findViewById(R.id.etUpdatePassword);
        spRole = findViewById(R.id.spUpdateRole);
        spStatus = findViewById(R.id.spUpdateStatus);
        tvError = findViewById(R.id.tvUpdateError);
        progressBar = findViewById(R.id.progressBarUpdate);
        btnSave = findViewById(R.id.btnSaveUpdate);

        final ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                ROLES
        );
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRole.setAdapter(roleAdapter);

        final ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                STATUSES
        );
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(statusAdapter);

        btnSave.setOnClickListener(v -> saveUser());

        if (Objects.nonNull(userId)) {
            loadUserData();
        } else {
            finish();
        }
    }

    private void loadUserData() {
        setLoading(true);

        updateUserAppExecutors.diskIO().execute(() -> {
            try {
                final UserResponseDto user = getUserByIdUseCase.getById(new GetUserByIdQuery(userId));
                updateUserAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    if (Objects.nonNull(user)) {
                        etName.setText(user.name());
                        etEmail.setText(user.email());

                        if ("ADMIN".equalsIgnoreCase(user.role())) {
                            spRole.setSelection(1);
                        } else {
                            spRole.setSelection(0);
                        }

                        if ("INACTIVE".equalsIgnoreCase(user.status())) {
                            spStatus.setSelection(1);
                        } else if ("SUSPENDED".equalsIgnoreCase(user.status())) {
                            spStatus.setSelection(2);
                        } else {
                            spStatus.setSelection(0);
                        }
                    }
                });
            } catch (final Exception ex) {
                updateUserAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    final String msg = Objects.nonNull(ex.getMessage()) ? ex.getMessage() : getString(R.string.error);
                    handleOperationError(ex, () -> {
                        Toast.makeText(UpdateUserActivity.this, msg, Toast.LENGTH_LONG).show();
                        finish();
                    });
                });
            }
        });
    }

    private void saveUser() {
        final String name = etName.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String role = spRole.getSelectedItem() != null ? spRole.getSelectedItem().toString() : "USER";
        final String status = spStatus.getSelectedItem() != null ? spStatus.getSelectedItem().toString() : "ACTIVE";

        if (name.isEmpty() || email.isEmpty()) {
            showError("Todos los campos obligatorios deben estar diligenciados");
            return;
        }

        hideError();
        setLoading(true);

        updateUserAppExecutors.diskIO().execute(() -> {
            try {
                updateUserUseCase.update(new UpdateUserCommand(userId, name, email, password, role, status));
                updateUserAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    Toast.makeText(UpdateUserActivity.this, R.string.user_updated_success, Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (final Exception ex) {
                updateUserAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    final String msg = Objects.nonNull(ex.getMessage()) ? ex.getMessage() : getString(R.string.error);
                    handleOperationError(ex, () -> showError(msg));
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
