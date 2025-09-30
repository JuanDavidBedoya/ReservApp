package com.reservapp.juanb.juanm.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "mesas")
public class Mesa {

    @Id
    private UUID idMesa;
    private int numeroMesa;
    private int capacidad;

    @OneToMany(mappedBy = "mesa")
    private List<Reserva> reservas = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_estado")
    private Estado estado;

    public Mesa() {
    }

    public Mesa(UUID idMesa, int numeroMesa, int capacidad, List<Reserva> reservas, Estado estado) {
        this.idMesa = idMesa;
        this.numeroMesa = numeroMesa;
        this.capacidad = capacidad;
        this.reservas = reservas;
        this.estado = estado;
    }

    public UUID getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(UUID idMesa) {
        this.idMesa = idMesa;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
