package com.jcaa.appcrudusers.application;

import com.jcaa.appcrudusers.application.dto.command.CreateVueloCommand;
import com.jcaa.appcrudusers.application.dto.command.DeleteVueloCommand;
import com.jcaa.appcrudusers.application.dto.command.UpdateVueloCommand;
import com.jcaa.appcrudusers.application.dto.query.GetVueloByIdQuery;
import com.jcaa.appcrudusers.application.dto.query.ListVuelosQuery;
import com.jcaa.appcrudusers.application.dto.response.VueloResponseDto;
import com.jcaa.appcrudusers.application.service.CreateVueloService;
import com.jcaa.appcrudusers.application.service.DeleteVueloService;
import com.jcaa.appcrudusers.application.service.GetVueloByIdService;
import com.jcaa.appcrudusers.application.service.ListVuelosService;
import com.jcaa.appcrudusers.application.service.UpdateVueloService;
import com.jcaa.appcrudusers.domain.model.Vuelo;
import com.jcaa.appcrudusers.domain.port.out.CreateVueloPort;
import com.jcaa.appcrudusers.domain.port.out.DeleteVueloPort;
import com.jcaa.appcrudusers.domain.port.out.GetVueloByIdPort;
import com.jcaa.appcrudusers.domain.port.out.ListVuelosPort;
import com.jcaa.appcrudusers.domain.port.out.UpdateVueloPort;
import com.jcaa.appcrudusers.domain.vo.UserId;
import com.jcaa.appcrudusers.domain.vo.VueloId;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class VueloApplicationServicesTest {

    private Vuelo createSampleVuelo(String id) {
        return new Vuelo(
                new VueloId(id),
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
    }

    @Test
    public void testCreateVueloService() {
        Vuelo expected = createSampleVuelo("v-1");
        CreateVueloPort port = registration -> {
            assertEquals("v-1", registration.getId().get().getValue());
            assertEquals("Avianca", registration.getAerolinea());
            return expected;
        };

        CreateVueloService service = new CreateVueloService(port);
        VueloResponseDto dto = service.create(new CreateVueloCommand(
                "v-1",
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
                "u-1"
        ));

        assertEquals("v-1", dto.id());
        assertEquals("Avianca", dto.aerolinea());
        assertEquals("AV123", dto.numero());
        assertEquals(Double.valueOf(350000.0), dto.valor());
    }

    @Test
    public void testGetVueloByIdService() {
        Vuelo expected = createSampleVuelo("v-2");
        GetVueloByIdPort port = id -> {
            assertEquals("v-2", id.getValue());
            return expected;
        };

        GetVueloByIdService service = new GetVueloByIdService(port);
        VueloResponseDto dto = service.getById(new GetVueloByIdQuery("v-2"));

        assertEquals("v-2", dto.id());
        assertEquals("Avianca", dto.aerolinea());
    }

    @Test
    public void testListVuelosService() {
        Vuelo expected = createSampleVuelo("v-3");
        ListVuelosPort port = () -> Collections.singletonList(expected);

        ListVuelosService service = new ListVuelosService(port);
        List<VueloResponseDto> list = service.list(new ListVuelosQuery());

        assertEquals(1, list.size());
        assertEquals("v-3", list.get(0).id());
    }

    @Test
    public void testUpdateVueloService() {
        Vuelo expected = createSampleVuelo("v-4");
        UpdateVueloPort port = (id, update) -> {
            assertEquals("v-4", id.getValue());
            assertEquals("Avianca", update.getAerolinea());
            return expected;
        };

        UpdateVueloService service = new UpdateVueloService(port);
        VueloResponseDto dto = service.update("v-4", new UpdateVueloCommand(
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
                "u-1"
        ));

        assertEquals("v-4", dto.id());
    }

    @Test
    public void testDeleteVueloService() {
        boolean[] deleted = {false};
        DeleteVueloPort port = id -> {
            assertEquals("v-5", id.getValue());
            deleted[0] = true;
        };

        DeleteVueloService service = new DeleteVueloService(port);
        service.delete(new DeleteVueloCommand("v-5"));

        assertTrue(deleted[0]);
    }
}
