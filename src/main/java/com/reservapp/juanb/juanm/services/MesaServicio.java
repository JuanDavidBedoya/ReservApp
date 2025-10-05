package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.entities.Mesa;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
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

    public Optional<Mesa> findById(UUID uuid) {
        return mesaRepositorio.findById(uuid);
    }

    public Mesa save(Mesa mesa) {
        try {
            return mesaRepositorio.save(mesa);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al guardar la mesa: " + e.getMessage());
        }
    }

    public void delete(UUID uuid) {
        try {
            mesaRepositorio.deleteById(uuid);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al eliminar la mesa: " + e.getMessage());
        }
    }

    public Mesa update(UUID uuid, Mesa mesa) {
        // Verificar que existe
        if (!mesaRepositorio.existsById(uuid)) {
            throw new ResourceNotFoundException("Mesa no encontrada con ID: " + uuid);
        }
        
        mesa.setIdMesa(uuid);
        return save(mesa);
    }

    // Métodos adicionales para validaciones
    public boolean existsByNumeroMesa(int numeroMesa) {
        return mesaRepositorio.existsByNumeroMesa(numeroMesa);
    }

    public boolean existsByNumeroMesaAndIdNot(int numeroMesa, UUID id) {
        return mesaRepositorio.existsByNumeroMesaAndIdMesaNot(numeroMesa, id);
    }
}