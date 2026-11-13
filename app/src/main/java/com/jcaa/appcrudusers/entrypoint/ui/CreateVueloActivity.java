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
import com.jcaa.appcrudusers.application.dto.command.CreateVueloCommand;
import com.jcaa.appcrudusers.application.dto.response.AuthSessionResponseDto;
import com.jcaa.appcrudusers.application.port.in.CreateVueloUseCase;
import com.jcaa.appcrudusers.application.port.in.GetSessionUseCase;
import com.jcaa.appcrudusers.entrypoint.util.AppExecutors;
import com.jcaa.appcrudusers.entrypoint.util.SessionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateVueloActivity extends BaseDrawerActivity {

    private static final String[] ESTADOS = new String[]{
            "CONFIRMADO", "PROGRAMADO", "PENDIENTE", "CANCELADO", "COMPLETADO"
    };

    private static final String DEFAULT_ADMIN_USER_ID = "00000000-0000-0000-0000-000000000001";

    @Inject
    CreateVueloUseCase createVueloUseCase;

    @Inject
    GetSessionUseCase getSessionUseCase;

    @Inject
    AppExecutors createVueloAppExecutors;

    private EditText etId;
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
        setDrawerContentView(R.layout.activity_create_vuelo);
        setDrawerTitle(getString(R.string.create_vuelo_title));
        enableUpNavigation();

        etId = findViewById(R.id.etCreateVueloId);
        etNumero = findViewById(R.id.etCreateVueloNumero);
        etAerolinea = findViewById(R.id.etCreateVueloAerolinea);
        etAgencia = findViewById(R.id.etCreateVueloAgencia);
        spEstado = findViewById(R.id.spCreateVueloEstado);
        etValor = findViewById(R.id.etCreateVueloValor);
        etCliente = findViewById(R.id.etCreateVueloCliente);
        etPuesto = findViewById(R.id.etCreateVueloPuesto);
        etAvion = findViewById(R.id.etCreateVueloAvion);
        etAereopuertoSalida = findViewById(R.id.etCreateVueloAereopuertoSalida);
        etAereopuertoLlegada = findViewById(R.id.etCreateVueloAereopuertoLlegada);
        etFechaCompra = findViewById(R.id.etCreateVueloFechaCompra);
        etFechaSalida = findViewById(R.id.etCreateVueloFechaSalida);
        etFechaLlegada = findViewById(R.id.etCreateVueloFechaLlegada);
        etPiloto = findViewById(R.id.etCreateVueloPiloto);
        etUsuarioId = findViewById(R.id.etCreateVueloUsuarioId);
        tvError = findViewById(R.id.tvCreateVueloError);
        progressBar = findViewById(R.id.progressBarCreateVuelo);
        btnSave = findViewById(R.id.btnSaveCreateVuelo);

        final ArrayAdapter<String> estadoAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                ESTADOS
        );
        estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEstado.setAdapter(estadoAdapter);

        // Pre-fill current date for fechaCompra
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        etFechaCompra.setText(sdf.format(new Date()));

        loadActiveSessionUser();

        btnSave.setOnClickListener(v -> saveVuelo());
    }

    private void loadActiveSessionUser() {
        createVueloAppExecutors.diskIO().execute(() -> {
            final Optional<AuthSessionResponseDto> sessionOpt = getSessionUseCase.getSession();
            String resolvedUserId = DEFAULT_ADMIN_USER_ID;
            if (sessionOpt.isPresent()) {
                final String extractedId = SessionUtils.extractUserIdFromToken(sessionOpt.get().accessToken());
                if (extractedId != null && !extractedId.isEmpty()) {
                    resolvedUserId = extractedId;
                }
            }
            final String finalUserId = resolvedUserId;
            createVueloAppExecutors.mainThread().execute(() -> {
                if (!isFinishing() && !isDestroyed()) {
                    etUsuarioId.setText(finalUserId);
                }
            });
        });
    }

    private void saveVuelo() {
        String id = etId.getText().toString().trim();
        if (id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }
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

        final String finalId = id;
        createVueloAppExecutors.diskIO().execute(() -> {
            try {
                final CreateVueloCommand command = new CreateVueloCommand(
                        finalId, fechaCompra, fechaSalida, fechaLlegada, agencia,
                        aerolinea, numero, estado, valor, cliente, puesto,
                        avion, salida, llegada, piloto, usuarioId
                );
                createVueloUseCase.create(command);
                createVueloAppExecutors.mainThread().execute(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    setLoading(false);
                    Toast.makeText(CreateVueloActivity.this, R.string.vuelo_created_success, Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (final Exception ex) {
                createVueloAppExecutors.mainThread().execute(() -> {
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
