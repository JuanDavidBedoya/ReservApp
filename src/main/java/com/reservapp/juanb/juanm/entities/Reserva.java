package com.reservapp.juanb.juanm.entities;

import java.sql.Date;
import java.sql.Time;
import java.util.UUID;

import jakarta.persistence.Entity;

@Entity
public class Reserva {

    private UUID idReserva;
    private Estado estado;
    private Usuario Usuario;
    private Mesa mesa;
    private Pago pago;
    private Date fecha;
    private Time hora;
    private int numeroPersonas;

    public Reserva() {
    }

    public Reserva(UUID idReserva, Estado estado, Usuario usuario, Mesa mesa,
            Pago pago, Date fecha, Time hora, int numeroPersonas) {
        this.idReserva = idReserva;
        this.estado = estado;
        this.usuario = usuario;
        this.mesa = mesa;
        this.pago = pago;
        this.fecha = fecha;
        this.hora = hora;
        this.numeroPersonas = numeroPersonas;
    }

    public UUID getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(UUID idReserva) {
        this.idReserva = idReserva;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return Usuario;
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

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public int getNumeroPersonas() {
        return numeroPersonas;
    }

    public void setNumeroPersonas(int numeroPersonas) {
        this.numeroPersonas = numeroPersonas;
    }    
}
