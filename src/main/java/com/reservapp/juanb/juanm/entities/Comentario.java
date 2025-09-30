package com.reservapp.juanb.juanm.entities;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "comentarios")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_comentario")
    private UUID idComentario;

    @Column(name = "puntuacion")
    private int puntuacion;

    @Column(name = "mensaje")
    private String mensaje;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_comentario")
    private Date fechaComentario;

    @ManyToOne
    @JoinColumn(name = "cedula_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_reserva")
    private Reserva reserva;

    public Comentario() {
    }

    public Comentario(UUID idComentario, int puntuacion, String mensaje, Date fechaComentario, Usuario usuario,
            Reserva reserva) {
        this.idComentario = idComentario;
        this.puntuacion = puntuacion;
        this.mensaje = mensaje;
        this.fechaComentario = fechaComentario;
        this.usuario = usuario;
        this.reserva = reserva;
    }

    public UUID getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(UUID idComentario) {
        this.idComentario = idComentario;
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
}
