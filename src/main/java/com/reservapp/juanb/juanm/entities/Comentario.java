package com.reservapp.juanb.juanm.entities;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Entity;

@Entity
public class Comentario {

    private UUID idComentario;
    private Usuario usuario;
    private Reserva reserva;
    private int puntuacion;
    private String mensaje;
    private Date fechaComentario;

    public Comentario() {
    }

    public Comentario(UUID idComentario, Usuario usuario, Reserva reserva, int puntuacion, String mensaje,
            Date fechaComentario) {
        this.idComentario = idComentario;
        this.usuario = usuario;
        this.reserva = reserva;
        this.puntuacion = puntuacion;
        this.mensaje = mensaje;
        this.fechaComentario = fechaComentario;
    }

    public UUID getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(UUID idComentario) {
        this.idComentario = idComentario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFechaComentario() {
        return fechaComentario;
    }

    public void setFechaComentario(Date fechaComentario) {
        this.fechaComentario = fechaComentario;
    }
}
