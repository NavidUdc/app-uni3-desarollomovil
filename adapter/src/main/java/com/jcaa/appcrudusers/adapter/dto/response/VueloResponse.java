package com.jcaa.appcrudusers.adapter.dto.response;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public final class VueloResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("fechaCompra")
    private String fechaCompra;

    @SerializedName("fechaSalida")
    private String fechaSalida;

    @SerializedName("fechaLlegada")
    private String fechaLlegada;

    @SerializedName("agenciaViajes")
    private String agenciaViajes;

    @SerializedName("aerolinea")
    private String aerolinea;

    @SerializedName("numero")
    private String numero;

    @SerializedName("estado")
    private String estado;

    @SerializedName("valor")
    private Double valor;

    @SerializedName("cliente")
    private String cliente;

    @SerializedName("puesto")
    private String puesto;

    @SerializedName("avion")
    private String avion;

    @SerializedName("aereopuertoSalida")
    private String aereopuertoSalida;

    @SerializedName("aereopuertoLlegada")
    private String aereopuertoLlegada;

    @SerializedName("piloto")
    private String piloto;

    @SerializedName("usuarioId")
    private String usuarioId;
}
