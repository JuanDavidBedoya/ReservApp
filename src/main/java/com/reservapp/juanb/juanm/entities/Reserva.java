package com.reservapp.juanb.juanm.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_reserva")
    private UUID idReserva;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha")
    private LocalDate fecha;

    @Temporal(TemporalType.TIME)
    @Column(name = "hora")
    private LocalTime hora;

    @Column(name = "numero_personas")
    private int numeroPersonas;

    @ManyToOne
    @JoinColumn(name = "cedula_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_mesa")
    private Mesa mesa;

    @ManyToOne
    @JoinColumn(name = "id_estado")
    private Estado estado;

    @OneToMany(mappedBy = "reserva")
    private List<Notificacion> notificaciones = new ArrayList<>();

    @Column(name = "recordatorio_24h_enviado", nullable = false)
    private boolean recordatorio24hEnviado = false;

    @Column(name = "recordatorio_1h_enviado", nullable = false)
    private boolean recordatorio1hEnviado = false;

    @Column(name = "encuesta_enviada", nullable = false)
    private boolean encuestaEnviada = false;

    public Reserva() {
    }

    public Reserva(UUID idReserva, LocalDate fecha, LocalTime hora, int numeroPersonas, Usuario usuario, Mesa mesa,
            Estado estado, List<Notificacion> notificaciones, boolean recordatorio24hEnviado, boolean recordatorio1hEnviado, boolean encuestaEnviada) {
        this.idReserva = idReserva;
        this.fecha = fecha;
        this.hora = hora;
        this.numeroPersonas = numeroPersonas;
        this.usuario = usuario;
        this.mesa = mesa;
        this.estado = estado;
        this.notificaciones = notificaciones;
        this.recordatorio24hEnviado = recordatorio24hEnviado;
        this.recordatorio1hEnviado = recordatorio1hEnviado;
        this.encuestaEnviada = encuestaEnviada;
    }

    public UUID getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(UUID idReserva) {
        this.idReserva = idReserva;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public int getNumeroPersonas() {
        return numeroPersonas;
    }

    public void setNumeroPersonas(int numeroPersonas) {
        this.numeroPersonas = numeroPersonas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }

    public boolean isRecordatorio24hEnviado() {
        return recordatorio24hEnviado;
    }

    public void setRecordatorio24hEnviado(boolean recordatorio24hEnviado) {
        this.recordatorio24hEnviado = recordatorio24hEnviado;
    }

    public boolean isRecordatorio1hEnviado() {
        return recordatorio1hEnviado;
    }

    public void setRecordatorio1hEnviado(boolean recordatorio1hEnviado) {
        this.recordatorio1hEnviado = recordatorio1hEnviado;
    }

    public boolean isEncuestaEnviada() {
        return encuestaEnviada;
    }

    public void setEncuestaEnviada(boolean encuestaEnviada) {
        this.encuestaEnviada = encuestaEnviada;
    }
} 