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
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_pago")
    private UUID idPago;
    
    @Column(name = "monto")
    private double monto;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_pago")
    private Date fechaPago;

    @ManyToOne
    @JoinColumn(name = "id_metodo")
    private Metodo metodo;

    @ManyToOne
    @JoinColumn(name = "id_estado")
    private Estado estado;

    public Pago() {
    }

    public Pago(UUID idPago, double monto, Date fechaPago, Metodo metodo, Estado estado) {
        this.idPago = idPago;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.metodo = metodo;
        this.estado = estado;
    }

    public UUID getIdPago() {
        return idPago;
    }

    public void setIdPago(UUID idPago) {
        this.idPago = idPago;
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
}
