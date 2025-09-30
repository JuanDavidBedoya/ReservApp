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
@Table(name = "tipos")
public class Tipo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_tipo")
    private UUID idTipo;

    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "tipo")
    private List<Notificacion> notificaciones = new ArrayList<>();

    public Tipo() {
    }

    public Tipo(UUID idTipo, String nombre, List<Notificacion> notificaciones) {
        this.idTipo = idTipo;
        this.nombre = nombre;
        this.notificaciones = notificaciones;
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

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }
}
