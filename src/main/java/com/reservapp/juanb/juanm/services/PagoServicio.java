package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.entities.Pago;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.repositories.PagoRepositorio;

@Service
public class PagoServicio {

    private PagoRepositorio pagoRepositorio;

    public PagoServicio(PagoRepositorio pagoRepositorio) {
        this.pagoRepositorio = pagoRepositorio;
    }

    public List<Pago> findAll() {
        return pagoRepositorio.findAll();
    }

    public Optional<Pago> findById(UUID uuid) {
        return pagoRepositorio.findById(uuid);
    }

    public Pago save(Pago pago) {
        try {
            return pagoRepositorio.save(pago);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al guardar el pago: " + e.getMessage());
        }
    }

    public void delete(UUID uuid) {
        try {
            pagoRepositorio.deleteById(uuid);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al eliminar el pago: " + e.getMessage());
        }
    }

    public Pago update(UUID uuid, Pago pago) {
        // Verificar que existe
        if (!pagoRepositorio.existsById(uuid)) {
            throw new ResourceNotFoundException("Pago no encontrado con ID: " + uuid);
        }
        
        pago.setIdPago(uuid);
        return save(pago);
    }
}