package com.reservapp.juanb.juanm.entities;

import jakarta.persistence.Entity;

@Entity
public class Usuario {

    private String cedula;
    private Rol rol;
    private String nombre;  
    private String correo;
    private String contrasena;
    private String telefono;

    public Usuario() {
    }

    public Usuario(String cedula, Rol rol, String nombre, String correo, String contrasena, String telefono) {
        this.cedula = cedula;
        this.rol = rol;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefono = telefono;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
