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
import com.jcaa.appcrudusers.application.dto.command.UpdateVueloCommand;
import com.jcaa.appcrudusers.application.dto.query.GetVueloByIdQuery;
import com.jcaa.appcrudusers.application.dto.response.VueloResponseDto;
import com.jcaa.appcrudusers.application.port.in.GetVueloByIdUseCase;
import com.jcaa.appcrudusers.application.port.in.UpdateVueloUseCase;
import com.jcaa.appcrudusers.entrypoint.util.AppExecutors;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UpdateVueloActivity extends BaseDrawerActivity {

    private static final String[] ESTADOS = new String[]{
            "CONFIRMADO", "PROGRAMADO", "PENDIENTE", "CANCELADO", "COMPLETADO"
    };

    @Inject
    UpdateVueloUseCase updateVueloUseCase;

    @Inject
    GetVueloByIdUseCase getVueloByIdUseCase;

    @Inject
    AppExecutors updateVueloAppExecutors;

    private String vueloId;

    private EditText etNumero;
    private EditText etAerolinea;
    private EditText etAgencia;
    private Spinner spEstado;
    private EditText etValor;
    private EditText etCliente;
    private EditText etPuesto;
    private EditText etAvion;
    private EditText etAereopuertoSalida;
    private EditText etAereopuertoLlegada;
    private EditText etFechaCompra;
    private EditText etFechaSalida;
    private EditText etFechaLlegada;
    private EditText etPiloto;
    private EditText etUsuarioId;
    private TextView tvError;
    private ProgressBar progressBar;
    private Button btnSave;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDrawerContentView(R.layout.activity_update_vuelo);
        setDrawerTitle(getString(R.string.edit_vuelo_title));
        enableUpNavigation();

        vueloId = getIntent().getStringExtra(VueloListActivity.EXTRA_VUELO_ID);

        etNumero = findViewById(R.id.etUpdateVueloNumero);
        etAerolinea = findViewById(R.id.etUpdateVueloAerolinea);
        etAgencia = findViewById(R.id.etUpdateVueloAgencia);
        spEstado = findViewById(R.id.spUpdateVueloEstado);
        etValor = findViewById(R.id.etUpdateVueloValor);
        etCliente = findViewById(R.id.etUpdateVueloCliente);
        etPuesto = findViewById(R.id.etUpdateVueloPuesto);
        etAvion = findViewById(R.id.etUpdateVueloAvion);
        etAereopuertoSalida = findViewById(R.id.etUpdateVueloAereopuertoSalida);
        etAereopuertoLlegada = findViewById(R.id.etUpdateVueloAereopuertoLlegada);
        etFechaCompra = findViewById(R.id.etUpdateVueloFechaCompra);
        etFechaSalida = findViewById(R.id.etUpdateVueloFechaSalida);
        etFechaLlegada = findViewById(R.id.etUpdateVueloFechaLlegada);
        etPiloto = findViewById(R.id.etUpdateVueloPiloto);
        etUsuarioId = findViewById(R.id.etUpdateVueloUsuarioId);
        tvError = findViewById(R.id.tvUpdateVueloError);
        progressBar = findViewById(R.id.progressBarUpdateVuelo);
        btnSave = findViewById(R.id.btnSaveUpdateVuelo);

        final ArrayAdapter<String> estadoAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                ESTADOS
        );
        estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEstado.setAdapter(estadoAdapter);

        btnSave.setOnClickListener(v -> saveVuelo());

        if (Objects.nonNull(vueloId)) {
            loadVueloData();
        } else {
            finish();
        }
    }

    private void loadVueloData() {
        setLoading(true);

        updateVueloAppExecutors.diskIO().execute(() -> {
            try {
                final VueloResponseDto vuelo = getVueloByIdUseCase.getById(new GetVueloByIdQuery(vueloId));
                updateVueloAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    if (Objects.nonNull(vuelo)) {
                        etNumero.setText(vuelo.numero());
                        etAerolinea.setText(vuelo.aerolinea());
                        etAgencia.setText(vuelo.agenciaViajes());
                        selectSpinnerValue(spEstado, vuelo.estado());
                        etValor.setText(String.valueOf(vuelo.valor() != null ? vuelo.valor() : 0.0));
                        etCliente.setText(vuelo.cliente());
                        etPuesto.setText(vuelo.puesto());
                        etAvion.setText(vuelo.avion());
                        etAereopuertoSalida.setText(vuelo.aereopuertoSalida());
                        etAereopuertoLlegada.setText(vuelo.aereopuertoLlegada());
                        etFechaCompra.setText(vuelo.fechaCompra());
                        etFechaSalida.setText(vuelo.fechaSalida());
                        etFechaLlegada.setText(vuelo.fechaLlegada());
                        etPiloto.setText(vuelo.piloto());
                        etUsuarioId.setText(vuelo.usuarioId());
                    } else {
                        Toast.makeText(UpdateVueloActivity.this, "Vuelo no encontrado", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            } catch (final Exception ex) {
                updateVueloAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    handleOperationError(ex, () -> {
                        final String msg = Objects.nonNull(ex.getMessage()) ? ex.getMessage() : getString(R.string.error);
                        Toast.makeText(UpdateVueloActivity.this, msg, Toast.LENGTH_LONG).show();
                        finish();
                    });
                });
            }
        });
    }

    private void saveVuelo() {
        final String numero = etNumero.getText().toString().trim();
        final String aerolinea = etAerolinea.getText().toString().trim();
        final String agencia = etAgencia.getText().toString().trim();
        final String estado = spEstado.getSelectedItem() != null ? spEstado.getSelectedItem().toString() : "CONFIRMADO";
        final String valorStr = etValor.getText().toString().trim();
        final String cliente = etCliente.getText().toString().trim();
        final String puesto = etPuesto.getText().toString().trim();
        final String avion = etAvion.getText().toString().trim();
        final String salida = etAereopuertoSalida.getText().toString().trim();
        final String llegada = etAereopuertoLlegada.getText().toString().trim();
        final String fechaCompra = etFechaCompra.getText().toString().trim();
        final String fechaSalida = etFechaSalida.getText().toString().trim();
        final String fechaLlegada = etFechaLlegada.getText().toString().trim();
        final String piloto = etPiloto.getText().toString().trim();
        final String usuarioId = etUsuarioId.getText().toString().trim();

        if (numero.isEmpty() || aerolinea.isEmpty() || agencia.isEmpty()
                || valorStr.isEmpty() || cliente.isEmpty() || puesto.isEmpty()
                || avion.isEmpty() || salida.isEmpty() || llegada.isEmpty()
                || fechaCompra.isEmpty() || fechaSalida.isEmpty() || fechaLlegada.isEmpty()
                || piloto.isEmpty() || usuarioId.isEmpty()) {
            showError("Todos los campos marcados con (*) son obligatorios");
            return;
        }

        final Double valor;
        try {
            valor = Double.parseDouble(valorStr);
            if (valor < 0) {
                showError("El valor del vuelo debe ser positivo");
                return;
            }
        } catch (final NumberFormatException e) {
            showError("Ingrese un valor numérico válido");
            return;
        }

        hideError();
        setLoading(true);

        updateVueloAppExecutors.diskIO().execute(() -> {
            try {
                final UpdateVueloCommand command = new UpdateVueloCommand(
                        fechaCompra, fechaSalida, fechaLlegada, agencia,
                        aerolinea, numero, estado, valor, cliente, puesto,
                        avion, salida, llegada, piloto, usuarioId
                );
                updateVueloUseCase.update(vueloId, command);
                updateVueloAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    Toast.makeText(UpdateVueloActivity.this, R.string.vuelo_updated_success, Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (final Exception ex) {
                updateVueloAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    handleOperationError(ex, () -> {
                        final String msg = Objects.nonNull(ex.getMessage()) ? ex.getMessage() : getString(R.string.error);
                        showError(msg);
                    });
                });
            }
        });
    }

    private void selectSpinnerValue(final Spinner spinner, final String value) {
        if (value == null || spinner == null) {
            return;
        }
        for (int i = 0; i < spinner.getCount(); i++) {
            if (value.equalsIgnoreCase(spinner.getItemAtPosition(i).toString())) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void showError(final String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        tvError.setVisibility(View.GONE);
    }

    private void setLoading(final boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnSave.setEnabled(!loading);
    }
}
