package com.jcaa.appcrudusers.application.dto.command;

public record UpdateVueloCommand(
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
