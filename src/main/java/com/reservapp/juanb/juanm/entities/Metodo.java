package com.reservapp.juanb.juanm.entities;

import java.util.UUID;

import jakarta.persistence.Entity;

@Entity
public class Metodo {

    private UUID idMetodo;
    private String nombre;

    public Metodo() {
    }

    public Metodo(UUID idMetodo, String nombre) {
        this.idMetodo = idMetodo;
        this.nombre = nombre;
    }

    public UUID getIdMetodo() {
        return idMetodo;
    }

    public void setIdMetodo(UUID idMetodo) {
        this.idMetodo = idMetodo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
