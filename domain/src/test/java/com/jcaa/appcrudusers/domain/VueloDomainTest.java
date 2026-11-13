package com.jcaa.appcrudusers.domain;

import com.jcaa.appcrudusers.domain.exception.ValidationException;
import com.jcaa.appcrudusers.domain.model.Vuelo;
import com.jcaa.appcrudusers.domain.model.VueloRegistration;
import com.jcaa.appcrudusers.domain.model.VueloUpdate;
import com.jcaa.appcrudusers.domain.vo.UserId;
import com.jcaa.appcrudusers.domain.vo.VueloId;
import java.util.Optional;
import org.junit.Test;
import static org.junit.Assert.*;

public class VueloDomainTest {

    @Test
    public void rejectsMissingOrEmptyVueloId() {
        assertThrows(ValidationException.class, () -> new VueloId(null));
        assertThrows(ValidationException.class, () -> new VueloId("   "));
        assertEquals("VUELO-123", new VueloId("VUELO-123").getValue());
    }

    @Test
    public void createsValidVueloModel() {
        Vuelo vuelo = new Vuelo(
                new VueloId("v-1"),
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

        assertEquals("v-1", vuelo.getId().getValue());
        assertEquals("Avianca", vuelo.getAerolinea());
        assertEquals("AV123", vuelo.getNumero());
        assertEquals(Double.valueOf(350000.0), vuelo.getValor());
        assertEquals("u-1", vuelo.getUsuarioId().getValue());
    }

    @Test
    public void createsValidVueloRegistrationAndAllowsOptionalId() {
        VueloRegistration reg = new VueloRegistration(
                Optional.of(new VueloId("v-custom")),
                "2026-11-13 10:00:00",
                "2026-11-15 08:00:00",
                "2026-11-15 10:30:00",
                "Despegar",
                "LATAM",
                "LA456",
                "CONFIRMADO",
                420000.0,
                "Maria Lopez",
                "3A",
                "Airbus A320",
                "MDE",
                "BOG",
                "Capitán Rodriguez",
                new UserId("u-1")
        );

        assertTrue(reg.getId().isPresent());
        assertEquals("v-custom", reg.getId().get().getValue());
        assertEquals("LATAM", reg.getAerolinea());
    }

    @Test
    public void createsValidVueloUpdate() {
        VueloUpdate update = new VueloUpdate(
                "2026-11-13 10:00:00",
                "2026-11-15 09:00:00",
                "2026-11-15 11:30:00",
                "Despegar",
                "Avianca",
                "AV999",
                "REPROGRAMADO",
                380000.0,
                "Carlos Gomez",
                "15C",
                "Boeing 737",
                "BOG",
                "CTG",
                "Capitán Perez",
                new UserId("u-1")
        );

        assertEquals("REPROGRAMADO", update.getEstado());
        assertEquals("15C", update.getPuesto());
        assertEquals(Double.valueOf(380000.0), update.getValor());
    }
}
