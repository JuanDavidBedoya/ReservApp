package com.reservapp.juanb.juanm.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_rol")
    private UUID idRol;

    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "rol")
    @JoinColumn(name = "usuarios", referencedColumnName = "cedula")
    private List<Usuario> usuarios = new ArrayList<>();

    public Rol() {
    }

    public Rol(UUID idRol, String nombre, List<Usuario> usuarios) {
        this.idRol = idRol;
        this.nombre = nombre;
        this.usuarios = usuarios;
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

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}