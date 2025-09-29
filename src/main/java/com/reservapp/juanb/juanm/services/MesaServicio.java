package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.entities.Mesa;
import com.reservapp.juanb.juanm.repositories.MesaRepositorio;

@Service
public class MesaServicio {

    private MesaRepositorio mesaRepositorio;

    public MesaServicio(MesaRepositorio mesaRepositorio) {
        this.mesaRepositorio = mesaRepositorio;
    }

    public List<Mesa> findAll() {
        return mesaRepositorio.findAll();
    }

    public Optional<Mesa> findById(java.util.UUID uuid) {
        return mesaRepositorio.findById(uuid);
    }

    public Mesa save(Mesa mesa) {
        return mesaRepositorio.save(mesa);
    }

    public void delete(java.util.UUID uuid) {
        mesaRepositorio.deleteById(uuid);
    }

    public Mesa update(java.util.UUID uuid, Mesa mesa) {
        mesa.setIdMesa(uuid);
        return mesaRepositorio.save(mesa);
    }
}
