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
@Table(name = "metodos")
public class Metodo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_metodo")
    private UUID idMetodo;

    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "metodo")
    @JoinColumn(name = "id_pago", referencedColumnName = "id_pago")
    private List<Pago> pagos = new ArrayList<>();

    public Metodo() {
    }

    public Metodo(UUID idMetodo, String nombre, List<Pago> pagos) {
        this.idMetodo = idMetodo;
        this.nombre = nombre;
        this.pagos = pagos;
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

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }
}
