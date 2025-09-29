package com.reservapp.juanb.juanm.entities;

import java.util.UUID;

import jakarta.persistence.Entity;

@Entity
public class Rol {

    private UUID idRol;
    private String nombre;

    public Rol() {
    }

    public Rol(UUID idRol, String nombre) {
        this.idRol = idRol;
        this.nombre = nombre;
    }

    public UUID getIdRol() {
        return idRol;
    }

    public void setIdRol(UUID idRol) {
        this.idRol = idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}