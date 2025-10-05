package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.entities.Metodo;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
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

    public Optional<Metodo> findById(UUID uuid) {
        return metodoRepositorio.findById(uuid);
    }

    public Metodo save(Metodo metodo) {
        try {
            return metodoRepositorio.save(metodo);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al guardar el método: " + e.getMessage());
        }
    }

    public void delete(UUID uuid) {
        try {
            metodoRepositorio.deleteById(uuid);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al eliminar el método: " + e.getMessage());
        }
    }

    public Metodo update(UUID uuid, Metodo metodo) {
        // Verificar que existe
        if (!metodoRepositorio.existsById(uuid)) {
            throw new ResourceNotFoundException("Método no encontrado con ID: " + uuid);
        }
        
        metodo.setIdMetodo(uuid);
        return save(metodo);
    }

    // Métodos adicionales para validaciones
    public boolean existsByNombre(String nombre) {
        return metodoRepositorio.existsByNombre(nombre);
    }

    public boolean existsByNombreAndIdNot(String nombre, UUID id) {
        return metodoRepositorio.existsByNombreAndIdMetodoNot(nombre, id);
    }
}