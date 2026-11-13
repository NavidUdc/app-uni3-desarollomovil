package com.jcaa.appcrudusers.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.gson.Gson;
import com.jcaa.appcrudusers.adapter.http.ApiErrorMapper;
import com.jcaa.appcrudusers.adapter.http.ApiExecutor;
import com.jcaa.appcrudusers.adapter.http.ApiJsonConverterFactory;
import com.jcaa.appcrudusers.adapter.http.SessionAuthorization;
import com.jcaa.appcrudusers.adapter.http.VuelosApi;
import com.jcaa.appcrudusers.adapter.remote.CreateVueloAdapter;
import com.jcaa.appcrudusers.adapter.remote.DeleteVueloAdapter;
import com.jcaa.appcrudusers.adapter.remote.GetVueloByIdAdapter;
import com.jcaa.appcrudusers.adapter.remote.ListVuelosAdapter;
import com.jcaa.appcrudusers.adapter.remote.UpdateVueloAdapter;
import com.jcaa.appcrudusers.domain.model.AuthSession;
import com.jcaa.appcrudusers.domain.model.Vuelo;
import com.jcaa.appcrudusers.domain.model.VueloRegistration;
import com.jcaa.appcrudusers.domain.model.VueloUpdate;
import com.jcaa.appcrudusers.domain.port.out.LoadSessionPort;
import com.jcaa.appcrudusers.domain.vo.AccessToken;
import com.jcaa.appcrudusers.domain.vo.UserId;
import com.jcaa.appcrudusers.domain.vo.VueloId;
import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Retrofit;

class VueloRemoteAdaptersTest {

    private static final Instant NOW = Instant.parse("2026-10-26T12:00:00Z");
    private static final String VUELO_JSON = "{"
            + "\"id\":\"v-101\","
            + "\"fechaCompra\":\"2026-11-13 10:00:00\","
            + "\"fechaSalida\":\"2026-11-15 08:00:00\","
            + "\"fechaLlegada\":\"2026-11-15 10:30:00\","
            + "\"agenciaViajes\":\"Despegar\","
            + "\"aerolinea\":\"Avianca\","
            + "\"numero\":\"AV123\","
            + "\"estado\":\"CONFIRMADO\","
            + "\"valor\":350000.0,"
            + "\"cliente\":\"Carlos Gomez\","
            + "\"puesto\":\"14B\","
            + "\"avion\":\"Boeing 737\","
            + "\"aereopuertoSalida\":\"BOG\","
            + "\"aereopuertoLlegada\":\"CTG\","
            + "\"piloto\":\"Capitán Perez\","
            + "\"usuarioId\":\"u-1\""
            + "}";

    private MockWebServer server;
    private VuelosApi api;
    private ApiExecutor executor;
    private SessionAuthorization authorization;

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.start();

        final Gson gson = new Gson();
        executor = new ApiExecutor(new ApiErrorMapper(gson));
        final LoadSessionPort sessions = () -> Optional.of(
                new AuthSession(new AccessToken("valid-token"), "Bearer", 3600L, NOW)
        );
        authorization = new SessionAuthorization(sessions, Clock.fixed(NOW, ZoneOffset.UTC));

        api = new Retrofit.Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(new ApiJsonConverterFactory(gson))
                .build()
                .create(VuelosApi.class);
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    void createVueloSendsPostAndAuthorization() throws InterruptedException {
        server.enqueue(new MockResponse()
                .setResponseCode(201)
                .setHeader("Content-Type", "application/json")
                .setBody(VUELO_JSON));

        final CreateVueloAdapter adapter = new CreateVueloAdapter(api, executor, authorization);
        final VueloRegistration reg = new VueloRegistration(
                Optional.of(new VueloId("v-101")),
                "2026-11-13 10:00:00",
                "2026-11-15 08:00:00",
                "2026-11-15 10:30:00",
                "Despegar",
                "Avianca",
                "AV123",
                "CONFIRMADO",
                350000.0,
                "Carlos Gomez",
                "14B",
                "Boeing 737",
                "BOG",
                "CTG",
                "Capitán Perez",
                new UserId("u-1")
        );

        final Vuelo created = adapter.create(reg);
        assertNotNull(created);
        assertEquals("v-101", created.getId().getValue());
        assertEquals("Avianca", created.getAerolinea());

        final RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getPath()).isEqualTo("/api/vuelos");
        assertThat(request.getHeader("Authorization")).isEqualTo("Bearer valid-token");
    }

    @Test
    void listVuelosReturnsListOfVuelos() throws InterruptedException {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("[" + VUELO_JSON + "]"));

        final ListVuelosAdapter adapter = new ListVuelosAdapter(api, executor, authorization);
        final List<Vuelo> vuelos = adapter.list();

        assertThat(vuelos).hasSize(1);
        assertThat(vuelos.get(0).getId().getValue()).isEqualTo("v-101");

        final RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo("/api/vuelos");
    }

    @Test
    void getVueloByIdReturnsSingleVuelo() throws InterruptedException {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(VUELO_JSON));

        final GetVueloByIdAdapter adapter = new GetVueloByIdAdapter(api, executor, authorization);
        final Vuelo vuelo = adapter.getById(new VueloId("v-101"));

        assertThat(vuelo.getId().getValue()).isEqualTo("v-101");

        final RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo("/api/vuelos/v-101");
    }

    @Test
    void updateVueloSendsPut() throws InterruptedException {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(VUELO_JSON));

        final UpdateVueloAdapter adapter = new UpdateVueloAdapter(api, executor, authorization);
        final VueloUpdate update = new VueloUpdate(
                "2026-11-13 10:00:00",
                "2026-11-15 08:00:00",
                "2026-11-15 10:30:00",
                "Despegar",
                "Avianca",
                "AV123",
                "CONFIRMADO",
                350000.0,
                "Carlos Gomez",
                "14B",
                "Boeing 737",
                "BOG",
                "CTG",
                "Capitán Perez",
                new UserId("u-1")
        );

        final Vuelo result = adapter.update(new VueloId("v-101"), update);
        assertThat(result.getId().getValue()).isEqualTo("v-101");

        final RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo("PUT");
        assertThat(request.getPath()).isEqualTo("/api/vuelos/v-101");
    }

    @Test
    void deleteVueloSendsDelete() throws InterruptedException {
        server.enqueue(new MockResponse().setResponseCode(204));

        final DeleteVueloAdapter adapter = new DeleteVueloAdapter(api, executor, authorization);
        adapter.delete(new VueloId("v-101"));

        final RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo("DELETE");
        assertThat(request.getPath()).isEqualTo("/api/vuelos/v-101");
    }
}
