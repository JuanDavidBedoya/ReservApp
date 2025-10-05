package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.entities.Notificacion;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.repositories.NotificacionRepositorio;

@Service
public class NotificacionServicio {

    private NotificacionRepositorio notificacionRepositorio;

    public NotificacionServicio(NotificacionRepositorio notificacionRepositorio) {
        this.notificacionRepositorio = notificacionRepositorio;
    }

    public List<Notificacion> findAll() {
        return notificacionRepositorio.findAll();
    }

    public Optional<Notificacion> findById(UUID uuid) {
        return notificacionRepositorio.findById(uuid);
    }

    public Notificacion save(Notificacion notificacion) {
        try {
            return notificacionRepositorio.save(notificacion);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al guardar la notificación: " + e.getMessage());
        }
    }

    public void delete(UUID uuid) {
        try {
            notificacionRepositorio.deleteById(uuid);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al eliminar la notificación: " + e.getMessage());
        }
    }

    public Notificacion update(UUID uuid, Notificacion notificacion) {
        // Verificar que existe
        if (!notificacionRepositorio.existsById(uuid)) {
            throw new ResourceNotFoundException("Notificación no encontrada con ID: " + uuid);
        }
        
        notificacion.setIdNotificacion(uuid);
        return save(notificacion);
    }
}