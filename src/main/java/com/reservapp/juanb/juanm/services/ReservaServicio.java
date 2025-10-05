package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.entities.Reserva;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.repositories.ReservaRepositorio;

@Service
public class ReservaServicio {

    private ReservaRepositorio reservaRepositorio;

    public ReservaServicio(ReservaRepositorio reservaRepositorio) {
        this.reservaRepositorio = reservaRepositorio;
    }

    public List<Reserva> findAll() {
        return reservaRepositorio.findAll();
    }

    public Optional<Reserva> findById(UUID uuid) {
        return reservaRepositorio.findById(uuid);
    }

    public Reserva save(Reserva reserva) {
        try {
            return reservaRepositorio.save(reserva);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al guardar la reserva: " + e.getMessage());
        }
    }

    public void delete(UUID uuid) {
        try {
            reservaRepositorio.deleteById(uuid);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al eliminar la reserva: " + e.getMessage());
        }
    }

    public Reserva update(UUID uuid, Reserva reserva) {
        // Verificar que existe
        if (!reservaRepositorio.existsById(uuid)) {
            throw new ResourceNotFoundException("Reserva no encontrada con ID: " + uuid);
        }
        
        reserva.setIdReserva(uuid);
        return save(reserva);
    }

    // Método para validar capacidad (RF20)
    public boolean exceedsCapacity(Reserva reserva) {
        // Implementación básica - debes adaptarla a tu lógica de negocio
        // Por ejemplo: verificar si la mesa ya tiene reservas en esa fecha/hora
        // que superen la capacidad máxima
        
        // Ejemplo simplificado:
        if (reserva.getMesa() != null && reserva.getNumeroPersonas() > reserva.getMesa().getCapacidad()) {
            return true;
        }
        
        // Aquí deberías implementar la lógica real para verificar el aforo del 100%
        // Esto podría incluir contar todas las reservas para esa fecha/hora
        // y verificar que no superen la capacidad total del restaurante
        
        return false; // Cambiar por tu implementación real
    }
}