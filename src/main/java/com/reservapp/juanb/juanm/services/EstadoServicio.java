package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.entities.Estado;
import com.reservapp.juanb.juanm.repositories.EstadoRepositorio;

@Service
public class EstadoServicio {

    private EstadoRepositorio estadoRepositorio;

    public EstadoServicio(EstadoRepositorio estadoRepositorio) {
        this.estadoRepositorio = estadoRepositorio;
    }

    public List<Estado> findAll() {
        return estadoRepositorio.findAll();
    }

    public Optional<Estado> findById(java.util.UUID uuid) {
        return estadoRepositorio.findById(uuid);
    }

    public Estado save(Estado estado) {
        return estadoRepositorio.save(estado);
    }

    public void delete(java.util.UUID uuid) {
        estadoRepositorio.deleteById(uuid);
    }

    public Estado update(java.util.UUID uuid, Estado estado) {
        estado.setIdEstado(uuid);
        return estadoRepositorio.save(estado);
    }
}
