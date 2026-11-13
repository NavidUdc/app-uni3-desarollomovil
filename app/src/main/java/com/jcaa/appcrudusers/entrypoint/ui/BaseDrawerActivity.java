package com.jcaa.appcrudusers.entrypoint.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.jcaa.appcrudusers.R;
import com.jcaa.appcrudusers.application.port.in.LogoutUseCase;
import com.jcaa.appcrudusers.domain.enums.FailureReason;
import com.jcaa.appcrudusers.domain.exception.DomainException;
import com.jcaa.appcrudusers.entrypoint.util.AppExecutors;

import java.util.Objects;

import javax.inject.Inject;

public abstract class BaseDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Inject
    LogoutUseCase logoutUseCase;

    @Inject
    AppExecutors appExecutors;

    private DrawerLayout drawerLayout;
    private MaterialToolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base_drawer);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation_view);

        setSupportActionBar(toolbar);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        );
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (Objects.nonNull(drawerLayout) && drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    protected void setDrawerContentView(@LayoutRes final int layoutResID) {
        final FrameLayout contentFrame = findViewById(R.id.content_frame);
        LayoutInflater.from(this).inflate(layoutResID, contentFrame, true);
    }

    protected void setDrawerTitle(final CharSequence title) {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected void setSelectedMenuItem(final int itemId) {
        if (navigationView != null) {
            navigationView.setCheckedItem(itemId);
        }
    }

    protected void enableUpNavigation() {
        if (drawerToggle != null) {
            drawerToggle.setDrawerIndicatorEnabled(false);
        }
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public boolean isSessionExpired(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        if (ex instanceof final DomainException domainEx) {
            if (domainEx.getReason() == FailureReason.UNAUTHENTICATED) {
                return true;
            }
        }
        final String msg = ex.getMessage();
        if (msg != null) {
            final String lowerMsg = msg.toLowerCase(java.util.Locale.ROOT);
            return lowerMsg.contains("sesion") || lowerMsg.contains("unauthenticated")
                    || lowerMsg.contains("unauthorized") || lowerMsg.contains("401");
        }
        return false;
    }

    public void handleOperationError(final Throwable ex, final Runnable defaultAction) {
        if (isSessionExpired(ex)) {
            if (Objects.nonNull(appExecutors)) {
                appExecutors.diskIO().execute(() -> {
                    try {
                        if (Objects.nonNull(logoutUseCase)) {
                            logoutUseCase.logout();
                        }
                    } catch (final Exception ignored) {
                        // Ignore cleanup errors
                    }
                    appExecutors.mainThread().execute(() -> {
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }
                        Toast.makeText(BaseDrawerActivity.this, R.string.session_expired_msg, Toast.LENGTH_LONG).show();
                        navigateToLogin();
                    });
                });
            } else {
                Toast.makeText(BaseDrawerActivity.this, R.string.session_expired_msg, Toast.LENGTH_LONG).show();
                navigateToLogin();
            }
        } else if (Objects.nonNull(defaultAction)) {
            defaultAction.run();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        final int itemId = item.getItemId();

        drawerLayout.closeDrawer(GravityCompat.START);

        if (itemId == R.id.nav_home) {
            if (!WelcomeActivity.class.isAssignableFrom(this.getClass())) {
                final Intent intent = new Intent(this, WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
            return true;
        } else if (itemId == R.id.nav_users) {
            if (!UserListActivity.class.isAssignableFrom(this.getClass())) {
                final Intent intent = new Intent(this, UserListActivity.class);
                startActivity(intent);
            }
            return true;
        } else if (itemId == R.id.nav_create_user) {
            if (!CreateUserActivity.class.isAssignableFrom(this.getClass())) {
                final Intent intent = new Intent(this, CreateUserActivity.class);
                startActivity(intent);
            }
            return true;
        } else if (itemId == R.id.nav_vuelos) {
            if (!VueloListActivity.class.isAssignableFrom(this.getClass())) {
                final Intent intent = new Intent(this, VueloListActivity.class);
                startActivity(intent);
            }
            return true;
        } else if (itemId == R.id.nav_create_vuelo) {
            if (!CreateVueloActivity.class.isAssignableFrom(this.getClass())) {
                final Intent intent = new Intent(this, CreateVueloActivity.class);
                startActivity(intent);
            }
            return true;
        } else if (itemId == R.id.nav_logout) {
            confirmLogoutFromDrawer();
            return true;
        }

        return false;
    }

    private void confirmLogoutFromDrawer() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.btn_logout)
                .setMessage(R.string.logout_confirm)
                .setPositiveButton(R.string.confirm, (dialog, which) -> executeLogoutFromDrawer())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void executeLogoutFromDrawer() {
        if (Objects.isNull(logoutUseCase) || Objects.isNull(appExecutors)) {
            navigateToLogin();
            return;
        }

        appExecutors.diskIO().execute(() -> {
            try {
                logoutUseCase.logout();
            } catch (final Exception ignored) {
                // Ignore logout exceptions
            }
            appExecutors.mainThread().execute(() -> {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                Toast.makeText(BaseDrawerActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                navigateToLogin();
            });
        });
    }

    private void navigateToLogin() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
