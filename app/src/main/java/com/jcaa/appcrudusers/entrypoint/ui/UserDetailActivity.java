package com.jcaa.appcrudusers.entrypoint.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.jcaa.appcrudusers.R;
import com.jcaa.appcrudusers.application.dto.command.DeleteUserCommand;
import com.jcaa.appcrudusers.application.dto.query.GetUserByIdQuery;
import com.jcaa.appcrudusers.application.dto.response.UserResponseDto;
import com.jcaa.appcrudusers.application.port.in.DeleteUserUseCase;
import com.jcaa.appcrudusers.application.port.in.GetUserByIdUseCase;
import com.jcaa.appcrudusers.entrypoint.util.AppExecutors;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserDetailActivity extends BaseDrawerActivity {

    @Inject
    GetUserByIdUseCase getUserByIdUseCase;

    @Inject
    DeleteUserUseCase deleteUserUseCase;

    @Inject
    AppExecutors userDetailAppExecutors;

    private String userId;

    private ProgressBar progressBar;
    private View containerInfo;
    private TextView tvDetailId;
    private TextView tvDetailName;
    private TextView tvDetailEmail;
    private TextView tvDetailRole;
    private TextView tvDetailStatus;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDrawerContentView(R.layout.activity_user_detail);
        setDrawerTitle(getString(R.string.user_detail_title));
        enableUpNavigation();

        userId = getIntent().getStringExtra(UserListActivity.EXTRA_USER_ID);

        progressBar = findViewById(R.id.progressBarDetail);
        containerInfo = findViewById(R.id.containerUserDetailInfo);
        tvDetailId = findViewById(R.id.tvDetailId);
        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailEmail = findViewById(R.id.tvDetailEmail);
        tvDetailRole = findViewById(R.id.tvDetailRole);
        tvDetailStatus = findViewById(R.id.tvDetailStatus);

        final Button btnEdit = findViewById(R.id.btnEditDetail);
        final Button btnDelete = findViewById(R.id.btnDeleteDetail);

        btnEdit.setOnClickListener(v -> {
            if (Objects.nonNull(userId)) {
                final Intent intent = new Intent(UserDetailActivity.this, UpdateUserActivity.class);
                intent.putExtra(UserListActivity.EXTRA_USER_ID, userId);
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(v -> confirmDeleteUser());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Objects.nonNull(userId)) {
            loadUserDetail();
        } else {
            finish();
        }
    }

    private void loadUserDetail() {
        setLoading(true);

        userDetailAppExecutors.diskIO().execute(() -> {
            try {
                final UserResponseDto user = getUserByIdUseCase.getById(new GetUserByIdQuery(userId));
                userDetailAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    if (Objects.nonNull(user)) {
                        tvDetailId.setText(user.id());
                        tvDetailName.setText(user.name());
                        tvDetailEmail.setText(user.email());
                        tvDetailRole.setText(user.role());
                        tvDetailStatus.setText(user.status());
                    }
                });
            } catch (final Exception ex) {
                userDetailAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    final String msg = Objects.nonNull(ex.getMessage()) ? ex.getMessage() : getString(R.string.error);
                    handleOperationError(ex, () -> {
                        Toast.makeText(UserDetailActivity.this, msg, Toast.LENGTH_LONG).show();
                        finish();
                    });
                });
            }
        });
    }

    private void confirmDeleteUser() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_confirm_title)
                .setMessage(R.string.delete_confirm_msg)
                .setPositiveButton(R.string.confirm, (dialog, which) -> executeDeleteUser())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void executeDeleteUser() {
        setLoading(true);

        userDetailAppExecutors.diskIO().execute(() -> {
            try {
                deleteUserUseCase.delete(new DeleteUserCommand(userId));
                userDetailAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    Toast.makeText(UserDetailActivity.this, R.string.delete_success, Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (final Exception ex) {
                userDetailAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    final String msg = Objects.nonNull(ex.getMessage()) ? ex.getMessage() : getString(R.string.error);
                    handleOperationError(ex, () -> Toast.makeText(UserDetailActivity.this, msg, Toast.LENGTH_LONG).show());
                });
            }
        });
    }

    private void setLoading(final boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        containerInfo.setVisibility(loading ? View.GONE : View.VISIBLE);
    }
}
