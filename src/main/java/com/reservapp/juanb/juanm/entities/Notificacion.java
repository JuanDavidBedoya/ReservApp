package com.reservapp.juanb.juanm.entities;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Entity;

@Entity
public class Notificacion {

    private UUID idNotificacion;
    private Tipo tipo;
    private Reserva reserva;
    private Estado estado;
    private String mensaje;
    private Date fechaEnvio;

    public Notificacion() {
    }

    public Notificacion(UUID idNotificacion, Tipo tipo, Reserva reserva, Estado estado, String mensaje,
            Date fechaEnvio) {
        this.idNotificacion = idNotificacion;
        this.tipo = tipo;
        this.reserva = reserva;
        this.estado = estado;
        this.mensaje = mensaje;
        this.fechaEnvio = fechaEnvio;
    }

    public UUID getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(UUID idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
}
