package com.reservapp.juanb.juanm.entities;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Entity;

@Entity
public class Pago {

    private UUID idPago;
    private Metodo metodo;
    private Estado estado;
    private double monto;
    private Date fechaPago;

    public Pago() {
    }

    public Pago(UUID idPago, Metodo metodo, Estado estado, double monto, Date fechaPago) {
        this.idPago = idPago;
        this.metodo = metodo;
        this.estado = estado;
        this.monto = monto;
        this.fechaPago = fechaPago;
    }

    public UUID getIdPago() {
        return idPago;
    }

    public void setIdPago(UUID idPago) {
        this.idPago = idPago;
    }

    public Metodo getMetodo() {
        return metodo;
    }

    public void setMetodo(Metodo metodo) {
        this.metodo = metodo;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }
}
