package com.reservapp.juanb.juanm.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "estados")
public class Estado {

    @Id
    private UUID idEstado;
    private String nombre;

    @OneToMany(mappedBy = "estado")
    private List<Mesa> mesas = new ArrayList<>();

    @OneToMany(mappedBy = "estado")
    private List<Reserva> reservas = new ArrayList<>();

    @OneToMany(mappedBy = "estado")
    private List<Pago> pagos = new ArrayList<>();

    @OneToMany(mappedBy = "estado")
    private List<Notificacion> notificaciones = new ArrayList<>();

    public Estado() {
    }

    public Estado(UUID idEstado, String nombre, List<Mesa> mesas, List<Reserva> reservas, List<Pago> pagos,
            List<Notificacion> notificaciones) {
        this.idEstado = idEstado;
        this.nombre = nombre;
        this.mesas = mesas;
        this.reservas = reservas;
        this.pagos = pagos;
        this.notificaciones = notificaciones;
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

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }
}
