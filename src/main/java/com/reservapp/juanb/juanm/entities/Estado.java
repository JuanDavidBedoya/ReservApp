package com.reservapp.juanb.juanm.entities;

import java.util.UUID;

import jakarta.persistence.Entity;

@Entity
public class Estado {

    private UUID idEstado;
    private String nombre;

    public Estado() {
    }

    public Estado(UUID idEstado, String nombre) {
        this.idEstado = idEstado;
        this.nombre = nombre;
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
}
