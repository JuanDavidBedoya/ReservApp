package com.reservapp.juanb.juanm.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "estados")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_estado")
    private UUID idEstado;

    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "estado")
    private List<Mesa> mesas = new ArrayList<>();

    @OneToMany(mappedBy = "estado")
    private List<Reserva> reservas = new ArrayList<>();

    @OneToMany(mappedBy = "estado")
    private List<Pago> pagos = new ArrayList<>();

    public Estado() {
    }

    public Estado(UUID idEstado, String nombre, List<Mesa> mesas, List<Reserva> reservas, List<Pago> pagos) {
        this.idEstado = idEstado;
        this.nombre = nombre;
        this.mesas = mesas;
        this.reservas = reservas;
        this.pagos = pagos;
    }

    public UUID getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(UUID idEstado) {
        this.idEstado = idEstado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Mesa> getMesas() {
        return mesas;
    }

    public void setMesas(List<Mesa> mesas) {
        this.mesas = mesas;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }
}

