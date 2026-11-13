package com.jcaa.appcrudusers.entrypoint.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jcaa.appcrudusers.R;
import com.jcaa.appcrudusers.application.dto.command.DeleteUserCommand;
import com.jcaa.appcrudusers.application.dto.query.ListUsersQuery;
import com.jcaa.appcrudusers.application.dto.response.UserResponseDto;
import com.jcaa.appcrudusers.application.port.in.DeleteUserUseCase;
import com.jcaa.appcrudusers.application.port.in.ListUsersUseCase;
import com.jcaa.appcrudusers.entrypoint.ui.adapter.UserAdapter;
import com.jcaa.appcrudusers.entrypoint.util.AppExecutors;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserListActivity extends BaseDrawerActivity {

    public static final String EXTRA_USER_ID = "extra_user_id";

    @Inject
    ListUsersUseCase listUsersUseCase;

    @Inject
    DeleteUserUseCase deleteUserUseCase;

    @Inject
    AppExecutors userListAppExecutors;

    private ProgressBar progressBar;
    private TextView tvEmpty;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDrawerContentView(R.layout.activity_user_list);
        setDrawerTitle(getString(R.string.users_list_title));
        setSelectedMenuItem(R.id.nav_users);

        progressBar = findViewById(R.id.progressBarUsers);
        tvEmpty = findViewById(R.id.tvEmptyUsers);
        final RecyclerView rvUsers = findViewById(R.id.rvUsers);
        final Button btnRefresh = findViewById(R.id.btnRefreshUsers);
        final Button btnCreate = findViewById(R.id.btnCreateUser);

        userAdapter = new UserAdapter(
                user -> {
                    final Intent intent = new Intent(UserListActivity.this, UserDetailActivity.class);
                    intent.putExtra(EXTRA_USER_ID, user.id());
                    startActivity(intent);
                },
                user -> {
                    final Intent intent = new Intent(UserListActivity.this, UpdateUserActivity.class);
                    intent.putExtra(EXTRA_USER_ID, user.id());
                    startActivity(intent);
                },
                this::confirmDeleteUser
        );

        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setAdapter(userAdapter);

        btnRefresh.setOnClickListener(v -> loadUsers());
        btnCreate.setOnClickListener(v -> startActivity(new Intent(UserListActivity.this, CreateUserActivity.class)));

        final Button btnGoToVuelos = findViewById(R.id.btnGoToVuelos);
        if (btnGoToVuelos != null) {
            btnGoToVuelos.setOnClickListener(v -> startActivity(new Intent(UserListActivity.this, VueloListActivity.class)));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsers();
    }

    private void loadUsers() {
        setLoading(true);

        userListAppExecutors.diskIO().execute(() -> {
            try {
                final List<UserResponseDto> users = listUsersUseCase.list(new ListUsersQuery());
                userListAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    if (Objects.isNull(users) || users.isEmpty()) {
                        tvEmpty.setVisibility(View.VISIBLE);
                        userAdapter.setUsers(null);
                    } else {
                        tvEmpty.setVisibility(View.GONE);
                        userAdapter.setUsers(users);
                    }
                });
            } catch (final Exception ex) {
                userListAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    final String msg = Objects.nonNull(ex.getMessage()) ? ex.getMessage() : getString(R.string.error);
                    Toast.makeText(UserListActivity.this, msg, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void confirmDeleteUser(final UserResponseDto user) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_confirm_title)
                .setMessage(R.string.delete_confirm_msg)
                .setPositiveButton(R.string.confirm, (dialog, which) -> executeDeleteUser(user))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void executeDeleteUser(final UserResponseDto user) {
        setLoading(true);

        userListAppExecutors.diskIO().execute(() -> {
            try {
                deleteUserUseCase.delete(new DeleteUserCommand(user.id()));
                userListAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    Toast.makeText(UserListActivity.this, R.string.delete_success, Toast.LENGTH_SHORT).show();
                    loadUsers();
                });
            } catch (final Exception ex) {
                userListAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    final String msg = Objects.nonNull(ex.getMessage()) ? ex.getMessage() : getString(R.string.error);
                    Toast.makeText(UserListActivity.this, msg, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void setLoading(final boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }
}
