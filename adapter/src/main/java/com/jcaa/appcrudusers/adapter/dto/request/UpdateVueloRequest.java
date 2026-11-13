package com.jcaa.appcrudusers.adapter.dto.request;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class UpdateVueloRequest {
    @SerializedName("fechaCompra")
    private final String fechaCompra;

    @SerializedName("fechaSalida")
    private final String fechaSalida;

    @SerializedName("fechaLlegada")
    private final String fechaLlegada;

    @SerializedName("agenciaViajes")
    private final String agenciaViajes;

    @SerializedName("aerolinea")
    private final String aerolinea;

    @SerializedName("numero")
    private final String numero;

    @SerializedName("estado")
    private final String estado;

    @SerializedName("valor")
    private final Double valor;

    @SerializedName("cliente")
    private final String cliente;

    @SerializedName("puesto")
    private final String puesto;

    @SerializedName("avion")
    private final String avion;

    @SerializedName("aereopuertoSalida")
    private final String aereopuertoSalida;

    @SerializedName("aereopuertoLlegada")
    private final String aereopuertoLlegada;

    @SerializedName("piloto")
    private final String piloto;

    @SerializedName("usuarioId")
    private final String usuarioId;
}
