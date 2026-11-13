package com.jcaa.appcrudusers.domain.model;

import com.jcaa.appcrudusers.domain.vo.UserId;
import com.jcaa.appcrudusers.domain.vo.VueloId;
import java.util.Objects;

/** Domain entity representing a flight (Vuelo). */
public final class Vuelo {
    private final VueloId id;
    private final String fechaCompra;
    private final String fechaSalida;
    private final String fechaLlegada;
    private final String agenciaViajes;
    private final String aerolinea;
    private final String numero;
    private final String estado;
    private final Double valor;
    private final String cliente;
    private final String puesto;
    private final String avion;
    private final String aereopuertoSalida;
    private final String aereopuertoLlegada;
    private final String piloto;
    private final UserId usuarioId;

    public Vuelo(
            final VueloId id,
            final String fechaCompra,
            final String fechaSalida,
            final String fechaLlegada,
            final String agenciaViajes,
            final String aerolinea,
            final String numero,
            final String estado,
            final Double valor,
            final String cliente,
            final String puesto,
            final String avion,
            final String aereopuertoSalida,
            final String aereopuertoLlegada,
            final String piloto,
            final UserId usuarioId
    ) {
        this.id = Objects.requireNonNull(id, "id");
        this.fechaCompra = Objects.requireNonNull(fechaCompra, "fechaCompra");
        this.fechaSalida = Objects.requireNonNull(fechaSalida, "fechaSalida");
        this.fechaLlegada = Objects.requireNonNull(fechaLlegada, "fechaLlegada");
        this.agenciaViajes = Objects.requireNonNull(agenciaViajes, "agenciaViajes");
        this.aerolinea = Objects.requireNonNull(aerolinea, "aerolinea");
        this.numero = Objects.requireNonNull(numero, "numero");
        this.estado = Objects.requireNonNull(estado, "estado");
        this.valor = Objects.requireNonNull(valor, "valor");
        this.cliente = Objects.requireNonNull(cliente, "cliente");
        this.puesto = Objects.requireNonNull(puesto, "puesto");
        this.avion = Objects.requireNonNull(avion, "avion");
        this.aereopuertoSalida = Objects.requireNonNull(aereopuertoSalida, "aereopuertoSalida");
        this.aereopuertoLlegada = Objects.requireNonNull(aereopuertoLlegada, "aereopuertoLlegada");
        this.piloto = Objects.requireNonNull(piloto, "piloto");
        this.usuarioId = Objects.requireNonNull(usuarioId, "usuarioId");
    }

    public VueloId getId() { return id; }
    public String getFechaCompra() { return fechaCompra; }
    public String getFechaSalida() { return fechaSalida; }
    public String getFechaLlegada() { return fechaLlegada; }
    public String getAgenciaViajes() { return agenciaViajes; }
    public String getAerolinea() { return aerolinea; }
    public String getNumero() { return numero; }
    public String getEstado() { return estado; }
    public Double getValor() { return valor; }
    public String getCliente() { return cliente; }
    public String getPuesto() { return puesto; }
    public String getAvion() { return avion; }
    public String getAereopuertoSalida() { return aereopuertoSalida; }
    public String getAereopuertoLlegada() { return aereopuertoLlegada; }
    public String getPiloto() { return piloto; }
    public UserId getUsuarioId() { return usuarioId; }
}
