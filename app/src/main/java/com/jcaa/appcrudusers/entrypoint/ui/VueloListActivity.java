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
import com.jcaa.appcrudusers.application.dto.command.DeleteVueloCommand;
import com.jcaa.appcrudusers.application.dto.query.ListVuelosQuery;
import com.jcaa.appcrudusers.application.dto.response.VueloResponseDto;
import com.jcaa.appcrudusers.application.port.in.DeleteVueloUseCase;
import com.jcaa.appcrudusers.application.port.in.ListVuelosUseCase;
import com.jcaa.appcrudusers.entrypoint.ui.adapter.VueloAdapter;
import com.jcaa.appcrudusers.entrypoint.util.AppExecutors;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class VueloListActivity extends BaseDrawerActivity {

    public static final String EXTRA_VUELO_ID = "extra_vuelo_id";

    @Inject
    ListVuelosUseCase listVuelosUseCase;

    @Inject
    DeleteVueloUseCase deleteVueloUseCase;

    @Inject
    AppExecutors vueloListAppExecutors;

    private ProgressBar progressBar;
    private TextView tvEmpty;
    private VueloAdapter vueloAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDrawerContentView(R.layout.activity_vuelo_list);
        setDrawerTitle(getString(R.string.vuelos_list_title));
        setSelectedMenuItem(R.id.nav_vuelos);

        progressBar = findViewById(R.id.progressBarVuelos);
        tvEmpty = findViewById(R.id.tvEmptyVuelos);
        final RecyclerView rvVuelos = findViewById(R.id.rvVuelos);
        final Button btnRefresh = findViewById(R.id.btnRefreshVuelos);
        final Button btnCreate = findViewById(R.id.btnCreateVuelo);

        vueloAdapter = new VueloAdapter(
                vuelo -> {
                    final Intent intent = new Intent(VueloListActivity.this, VueloDetailActivity.class);
                    intent.putExtra(EXTRA_VUELO_ID, vuelo.id());
                    startActivity(intent);
                },
                vuelo -> {
                    final Intent intent = new Intent(VueloListActivity.this, UpdateVueloActivity.class);
                    intent.putExtra(EXTRA_VUELO_ID, vuelo.id());
                    startActivity(intent);
                },
                this::confirmDeleteVuelo
        );

        rvVuelos.setLayoutManager(new LinearLayoutManager(this));
        rvVuelos.setAdapter(vueloAdapter);

        btnRefresh.setOnClickListener(v -> loadVuelos());
        btnCreate.setOnClickListener(v -> startActivity(new Intent(VueloListActivity.this, CreateVueloActivity.class)));

        final Button btnGoToUsers = findViewById(R.id.btnGoToUsers);
        if (btnGoToUsers != null) {
            btnGoToUsers.setOnClickListener(v -> startActivity(new Intent(VueloListActivity.this, UserListActivity.class)));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadVuelos();
    }

    private void loadVuelos() {
        setLoading(true);

        vueloListAppExecutors.diskIO().execute(() -> {
            try {
                final List<VueloResponseDto> vuelos = listVuelosUseCase.list(new ListVuelosQuery());
                vueloListAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    if (Objects.isNull(vuelos) || vuelos.isEmpty()) {
                        tvEmpty.setVisibility(View.VISIBLE);
                        vueloAdapter.setVuelos(null);
                    } else {
                        tvEmpty.setVisibility(View.GONE);
                        vueloAdapter.setVuelos(vuelos);
                    }
                });
            } catch (final Exception ex) {
                vueloListAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    handleOperationError(ex, () -> {
                        final String msg = Objects.nonNull(ex.getMessage()) ? ex.getMessage() : getString(R.string.error);
                        Toast.makeText(VueloListActivity.this, msg, Toast.LENGTH_LONG).show();
                    });
                });
            }
        });
    }

    private void confirmDeleteVuelo(final VueloResponseDto vuelo) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_vuelo_confirm_title)
                .setMessage(getString(R.string.delete_vuelo_confirm_msg) + "\n\n" + vuelo.aerolinea() + " " + vuelo.numero())
                .setPositiveButton(R.string.confirm, (dialog, which) -> executeDeleteVuelo(vuelo.id()))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void executeDeleteVuelo(final String id) {
        setLoading(true);

        vueloListAppExecutors.diskIO().execute(() -> {
            try {
                deleteVueloUseCase.delete(new DeleteVueloCommand(id));
                vueloListAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    Toast.makeText(VueloListActivity.this, R.string.delete_vuelo_success, Toast.LENGTH_SHORT).show();
                    loadVuelos();
                });
            } catch (final Exception ex) {
                vueloListAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    handleOperationError(ex, () -> {
                        final String msg = Objects.nonNull(ex.getMessage()) ? ex.getMessage() : getString(R.string.error);
                        Toast.makeText(VueloListActivity.this, msg, Toast.LENGTH_LONG).show();
                    });
                });
            }
        });
    }

    private void setLoading(final boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }
}
