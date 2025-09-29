package com.reservapp.juanb.juanm.entities;

import java.util.UUID;

import jakarta.persistence.Entity;

@Entity
public class Mesa {

    private UUID idMesa;
    private Estado estado;
    private int numeroMesa;
    private int capacidad;

    public Mesa() {
    }

    public Mesa(UUID idMesa, Estado estado, int numeroMesa, int capacidad) {
        this.idMesa = idMesa;
        this.estado = estado;
        this.numeroMesa = numeroMesa;
        this.capacidad = capacidad;
    }

    public UUID getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(UUID idMesa) {
        this.idMesa = idMesa;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
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

}
