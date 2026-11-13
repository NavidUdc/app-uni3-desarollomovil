package com.jcaa.appcrudusers.application.dto.command;

public record CreateVueloCommand(
        String id,
        String fechaCompra,
        String fechaSalida,
        String fechaLlegada,
        String agenciaViajes,
        String aerolinea,
        String numero,
        String estado,
        Double valor,
        String cliente,
        String puesto,
        String avion,
        String aereopuertoSalida,
        String aereopuertoLlegada,
        String piloto,
        String usuarioId
) {}
