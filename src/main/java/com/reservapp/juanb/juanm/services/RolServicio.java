package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.entities.Rol;
import com.reservapp.juanb.juanm.repositories.RolRepositorio;

@Service
public class RolServicio {

    private RolRepositorio rolRepositorio;

    public RolServicio(RolRepositorio rolRepositorio) {
        this.rolRepositorio = rolRepositorio;
    }

    public List<Rol> findAll() {
        return rolRepositorio.findAll();
    }

    public Optional<Rol> findById(java.util.UUID uuid) {
        return rolRepositorio.findById(uuid);
    }

    public Rol save(Rol rol) {
        return rolRepositorio.save(rol);
    }

    public void delete(java.util.UUID uuid) {
        rolRepositorio.deleteById(uuid);
    }

    public Rol update(java.util.UUID uuid, Rol rol) {
        rol.setIdRol(uuid);
        return rolRepositorio.save(rol);
    }
}
