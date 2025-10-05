package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.entities.Rol;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
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

    public Optional<Rol> findById(UUID uuid) {
        return rolRepositorio.findById(uuid);
    }

    public Rol save(Rol rol) {
        try {
            return rolRepositorio.save(rol);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al guardar el rol: " + e.getMessage());
        }
    }

    public void delete(UUID uuid) {
        try {
            rolRepositorio.deleteById(uuid);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al eliminar el rol: " + e.getMessage());
        }
    }

    public Rol update(UUID uuid, Rol rol) {
        // Verificar que existe
        if (!rolRepositorio.existsById(uuid)) {
            throw new ResourceNotFoundException("Rol no encontrado con ID: " + uuid);
        }
        
        rol.setIdRol(uuid);
        return save(rol);
    }

    // Métodos adicionales para validaciones
    public boolean existsByNombre(String nombre) {
        return rolRepositorio.existsByNombre(nombre);
    }

    public boolean existsByNombreAndIdNot(String nombre, UUID id) {
        return rolRepositorio.existsByNombreAndIdRolNot(nombre, id);
    }
}