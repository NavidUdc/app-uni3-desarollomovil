package com.jcaa.appcrudusers.adapter.mapper;

import com.jcaa.appcrudusers.adapter.dto.request.CreateVueloRequest;
import com.jcaa.appcrudusers.adapter.dto.request.UpdateVueloRequest;
import com.jcaa.appcrudusers.adapter.dto.response.VueloResponse;
import com.jcaa.appcrudusers.domain.model.Vuelo;
import com.jcaa.appcrudusers.domain.model.VueloRegistration;
import com.jcaa.appcrudusers.domain.model.VueloUpdate;
import com.jcaa.appcrudusers.domain.vo.UserId;
import com.jcaa.appcrudusers.domain.vo.VueloId;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class VueloMapper {

    public static CreateVueloRequest toRequest(final VueloRegistration vuelo) {
        return new CreateVueloRequest(
                vuelo.getId().map(VueloId::getValue).orElse(null),
                vuelo.getFechaCompra(),
                vuelo.getFechaSalida(),
                vuelo.getFechaLlegada(),
                vuelo.getAgenciaViajes(),
                vuelo.getAerolinea(),
                vuelo.getNumero(),
                vuelo.getEstado(),
                vuelo.getValor(),
                vuelo.getCliente(),
                vuelo.getPuesto(),
                vuelo.getAvion(),
                vuelo.getAereopuertoSalida(),
                vuelo.getAereopuertoLlegada(),
                vuelo.getPiloto(),
                vuelo.getUsuarioId().getValue()
        );
    }

    public static UpdateVueloRequest toRequest(final VueloUpdate vuelo) {
        return new UpdateVueloRequest(
                vuelo.getFechaCompra(),
                vuelo.getFechaSalida(),
                vuelo.getFechaLlegada(),
                vuelo.getAgenciaViajes(),
                vuelo.getAerolinea(),
                vuelo.getNumero(),
                vuelo.getEstado(),
                vuelo.getValor(),
                vuelo.getCliente(),
                vuelo.getPuesto(),
                vuelo.getAvion(),
                vuelo.getAereopuertoSalida(),
                vuelo.getAereopuertoLlegada(),
                vuelo.getPiloto(),
                vuelo.getUsuarioId().getValue()
        );
    }

    public static Vuelo toDomain(final VueloResponse response) {
        return new Vuelo(
                new VueloId(response.getId()),
                response.getFechaCompra(),
                response.getFechaSalida(),
                response.getFechaLlegada(),
                response.getAgenciaViajes(),
                response.getAerolinea(),
                response.getNumero(),
                response.getEstado(),
                response.getValor() != null ? response.getValor() : 0.0,
                response.getCliente(),
                response.getPuesto(),
                response.getAvion(),
                response.getAereopuertoSalida(),
                response.getAereopuertoLlegada(),
                response.getPiloto(),
                new UserId(response.getUsuarioId())
        );
    }

    public static List<Vuelo> toDomain(final List<VueloResponse> responses) {
        if (responses == null) {
            return Collections.emptyList();
        }
        return responses.stream()
                .map(VueloMapper::toDomain)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }
}
