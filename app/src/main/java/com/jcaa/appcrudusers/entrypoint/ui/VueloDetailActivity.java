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
import com.jcaa.appcrudusers.application.dto.command.DeleteVueloCommand;
import com.jcaa.appcrudusers.application.dto.query.GetVueloByIdQuery;
import com.jcaa.appcrudusers.application.dto.response.VueloResponseDto;
import com.jcaa.appcrudusers.application.port.in.DeleteVueloUseCase;
import com.jcaa.appcrudusers.application.port.in.GetVueloByIdUseCase;
import com.jcaa.appcrudusers.entrypoint.util.AppExecutors;

import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class VueloDetailActivity extends BaseDrawerActivity {

    @Inject
    GetVueloByIdUseCase getVueloByIdUseCase;

    @Inject
    DeleteVueloUseCase deleteVueloUseCase;

    @Inject
    AppExecutors vueloDetailAppExecutors;

    private String vueloId;

    private ProgressBar progressBar;
    private View containerInfo;
    private TextView tvDetailVueloId;
    private TextView tvDetailVueloNumero;
    private TextView tvDetailVueloAerolinea;
    private TextView tvDetailVueloAvion;
    private TextView tvDetailVueloEstado;
    private TextView tvDetailVueloValor;
    private TextView tvDetailVueloAereopuertoSalida;
    private TextView tvDetailVueloAereopuertoLlegada;
    private TextView tvDetailVueloFechaCompra;
    private TextView tvDetailVueloFechaSalida;
    private TextView tvDetailVueloFechaLlegada;
    private TextView tvDetailVueloCliente;
    private TextView tvDetailVueloPuesto;
    private TextView tvDetailVueloPiloto;
    private TextView tvDetailVueloAgencia;
    private TextView tvDetailVueloUsuarioId;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDrawerContentView(R.layout.activity_vuelo_detail);
        setDrawerTitle(getString(R.string.vuelo_detail_title));
        enableUpNavigation();

        vueloId = getIntent().getStringExtra(VueloListActivity.EXTRA_VUELO_ID);

        progressBar = findViewById(R.id.progressBarDetailVuelo);
        containerInfo = findViewById(R.id.containerVueloDetailInfo);

        tvDetailVueloId = findViewById(R.id.tvDetailVueloId);
        tvDetailVueloNumero = findViewById(R.id.tvDetailVueloNumero);
        tvDetailVueloAerolinea = findViewById(R.id.tvDetailVueloAerolinea);
        tvDetailVueloAvion = findViewById(R.id.tvDetailVueloAvion);
        tvDetailVueloEstado = findViewById(R.id.tvDetailVueloEstado);
        tvDetailVueloValor = findViewById(R.id.tvDetailVueloValor);
        tvDetailVueloAereopuertoSalida = findViewById(R.id.tvDetailVueloAereopuertoSalida);
        tvDetailVueloAereopuertoLlegada = findViewById(R.id.tvDetailVueloAereopuertoLlegada);
        tvDetailVueloFechaCompra = findViewById(R.id.tvDetailVueloFechaCompra);
        tvDetailVueloFechaSalida = findViewById(R.id.tvDetailVueloFechaSalida);
        tvDetailVueloFechaLlegada = findViewById(R.id.tvDetailVueloFechaLlegada);
        tvDetailVueloCliente = findViewById(R.id.tvDetailVueloCliente);
        tvDetailVueloPuesto = findViewById(R.id.tvDetailVueloPuesto);
        tvDetailVueloPiloto = findViewById(R.id.tvDetailVueloPiloto);
        tvDetailVueloAgencia = findViewById(R.id.tvDetailVueloAgencia);
        tvDetailVueloUsuarioId = findViewById(R.id.tvDetailVueloUsuarioId);

        final Button btnEdit = findViewById(R.id.btnEditDetailVuelo);
        final Button btnDelete = findViewById(R.id.btnDeleteDetailVuelo);

        btnEdit.setOnClickListener(v -> {
            if (Objects.nonNull(vueloId)) {
                final Intent intent = new Intent(VueloDetailActivity.this, UpdateVueloActivity.class);
                intent.putExtra(VueloListActivity.EXTRA_VUELO_ID, vueloId);
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(v -> confirmDeleteVuelo());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Objects.nonNull(vueloId)) {
            loadVueloDetail();
        } else {
            finish();
        }
    }

    private void loadVueloDetail() {
        setLoading(true);

        vueloDetailAppExecutors.diskIO().execute(() -> {
            try {
                final VueloResponseDto vuelo = getVueloByIdUseCase.getById(new GetVueloByIdQuery(vueloId));
                vueloDetailAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    if (Objects.nonNull(vuelo)) {
                        bindVueloData(vuelo);
                    } else {
                        Toast.makeText(VueloDetailActivity.this, "Vuelo no encontrado", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            } catch (final Exception ex) {
                vueloDetailAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    handleOperationError(ex, () -> {
                        final String msg = Objects.nonNull(ex.getMessage()) ? ex.getMessage() : getString(R.string.error);
                        Toast.makeText(VueloDetailActivity.this, msg, Toast.LENGTH_LONG).show();
                        finish();
                    });
                });
            }
        });
    }

    private void bindVueloData(final VueloResponseDto vuelo) {
        tvDetailVueloId.setText(vuelo.id());
        tvDetailVueloNumero.setText(vuelo.numero());
        tvDetailVueloAerolinea.setText(vuelo.aerolinea());
        tvDetailVueloAvion.setText(vuelo.avion());
        tvDetailVueloEstado.setText(vuelo.estado());
        final Double valor = vuelo.valor() != null ? vuelo.valor() : 0.0;
        tvDetailVueloValor.setText(String.format(Locale.getDefault(), "$%,.2f", valor));
        tvDetailVueloAereopuertoSalida.setText(vuelo.aereopuertoSalida());
        tvDetailVueloAereopuertoLlegada.setText(vuelo.aereopuertoLlegada());
        tvDetailVueloFechaCompra.setText(vuelo.fechaCompra());
        tvDetailVueloFechaSalida.setText(vuelo.fechaSalida());
        tvDetailVueloFechaLlegada.setText(vuelo.fechaLlegada());
        tvDetailVueloCliente.setText(vuelo.cliente());
        tvDetailVueloPuesto.setText(vuelo.puesto());
        tvDetailVueloPiloto.setText(vuelo.piloto());
        tvDetailVueloAgencia.setText(vuelo.agenciaViajes());
        tvDetailVueloUsuarioId.setText(vuelo.usuarioId());
    }

    private void confirmDeleteVuelo() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_vuelo_confirm_title)
                .setMessage(R.string.delete_vuelo_confirm_msg)
                .setPositiveButton(R.string.confirm, (dialog, which) -> executeDeleteVuelo())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void executeDeleteVuelo() {
        if (Objects.isNull(vueloId)) {
            return;
        }

        setLoading(true);

        vueloDetailAppExecutors.diskIO().execute(() -> {
            try {
                deleteVueloUseCase.delete(new DeleteVueloCommand(vueloId));
                vueloDetailAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    Toast.makeText(VueloDetailActivity.this, R.string.delete_vuelo_success, Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (final Exception ex) {
                vueloDetailAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    handleOperationError(ex, () -> {
                        final String msg = Objects.nonNull(ex.getMessage()) ? ex.getMessage() : getString(R.string.error);
                        Toast.makeText(VueloDetailActivity.this, msg, Toast.LENGTH_LONG).show();
                    });
                });
            }
        });
    }

    private void setLoading(final boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        containerInfo.setVisibility(loading ? View.GONE : View.VISIBLE);
    }
}
