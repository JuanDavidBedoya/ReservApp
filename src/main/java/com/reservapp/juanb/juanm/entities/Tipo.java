package com.reservapp.juanb.juanm.entities;

import java.util.UUID;

import jakarta.persistence.Entity;

@Entity
public class Tipo {

    private UUID idTipo;
    private String nombre;

    public Tipo() {
    }

    public Tipo(UUID idTipo, String nombre) {
        this.idTipo = idTipo;
        this.nombre = nombre;
    }

    public UUID getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(UUID idTipo) {
        this.idTipo = idTipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
