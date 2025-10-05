package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.entities.Tipo;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
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

    public Optional<Tipo> findById(UUID uuid) {
        return tipoRepositorio.findById(uuid);
    }

    public Tipo save(Tipo tipo) {
        try {
            return tipoRepositorio.save(tipo);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al guardar el tipo: " + e.getMessage());
        }
    }

    public void delete(UUID uuid) {
        try {
            tipoRepositorio.deleteById(uuid);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al eliminar el tipo: " + e.getMessage());
        }
    }

    public Tipo update(UUID uuid, Tipo tipo) {
        // Verificar que existe
        if (!tipoRepositorio.existsById(uuid)) {
            throw new ResourceNotFoundException("Tipo no encontrado con ID: " + uuid);
        }
        
        tipo.setIdTipo(uuid);
        return save(tipo);
    }

    // Métodos adicionales para validaciones
    public boolean existsByNombre(String nombre) {
        return tipoRepositorio.existsByNombre(nombre);
    }

    public boolean existsByNombreAndIdNot(String nombre, UUID id) {
        return tipoRepositorio.existsByNombreAndIdTipoNot(nombre, id);
    }
}