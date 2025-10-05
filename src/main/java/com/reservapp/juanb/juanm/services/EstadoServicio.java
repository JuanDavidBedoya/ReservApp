package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.entities.Estado;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
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

    public Optional<Estado> findById(UUID uuid) {
        return estadoRepositorio.findById(uuid);
    }

    public Estado save(Estado estado) {
        try {
            return estadoRepositorio.save(estado);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al guardar el estado: " + e.getMessage());
        }
    }

    public void delete(UUID uuid) {
        try {
            estadoRepositorio.deleteById(uuid);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al eliminar el estado: " + e.getMessage());
        }
    }

    public Estado update(UUID uuid, Estado estado) {
        // Verificar que existe
        if (!estadoRepositorio.existsById(uuid)) {
            throw new ResourceNotFoundException("Estado no encontrado con ID: " + uuid);
        }
        
        estado.setIdEstado(uuid);
        return save(estado);
    }

    // Métodos adicionales para validaciones
    public boolean existsByNombre(String nombre) {
        return estadoRepositorio.existsByNombre(nombre);
    }

    public boolean existsByNombreAndIdNot(String nombre, UUID id) {
        return estadoRepositorio.existsByNombreAndIdEstadoNot(nombre, id);
    }
}