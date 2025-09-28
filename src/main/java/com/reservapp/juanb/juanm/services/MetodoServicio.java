package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.entities.Metodo;
import com.reservapp.juanb.juanm.repositories.MetodoRepositorio;

@Service
public class MetodoServicio {

    private MetodoRepositorio metodoRepositorio;

    public MetodoServicio(MetodoRepositorio metodoRepositorio) {
        this.metodoRepositorio = metodoRepositorio;
    }

    public List<Metodo> findAll() {
        return metodoRepositorio.findAll();
    }

    public Optional<Metodo> findById(java.util.UUID uuid) {
        return metodoRepositorio.findById(uuid);
    }

    public Metodo save(Metodo metodo) {
        return metodoRepositorio.save(metodo);
    }

    public void delete(java.util.UUID uuid) {
        metodoRepositorio.deleteById(uuid);
    }

    public Metodo update(java.util.UUID uuid, Metodo metodo) {
        metodo.setIdMetodo(uuid);
        return metodoRepositorio.save(metodo);
    }
}
