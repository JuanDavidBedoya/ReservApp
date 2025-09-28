package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.entities.Tipo;
import com.reservapp.juanb.juanm.repositories.TipoRepositorio;

@Service
public class TipoServicio {

    private TipoRepositorio tipoRepositorio;

    public TipoServicio(TipoRepositorio tipoRepositorio) {
        this.tipoRepositorio = tipoRepositorio;
    }

    public List<Tipo> findAll() {
        return tipoRepositorio.findAll();
    }

    public Optional<Tipo> findById(java.util.UUID uuid) {
        return tipoRepositorio.findById(uuid);
    }

    public Tipo save(Tipo tipo) {
        return tipoRepositorio.save(tipo);
    }

    public void delete(java.util.UUID uuid) {
        tipoRepositorio.deleteById(uuid);
    }

    public Tipo update(java.util.UUID uuid, Tipo tipo) {
        tipo.setIdTipo(uuid);
        return tipoRepositorio.save(tipo);
    }
}
