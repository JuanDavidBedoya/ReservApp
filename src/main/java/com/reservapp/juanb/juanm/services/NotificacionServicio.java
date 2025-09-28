package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.entities.Notificacion;
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

    public Optional<Notificacion> findById(java.util.UUID uuid) {
        return notificacionRepositorio.findById(uuid);
    }

    public Notificacion save(Notificacion notificacion) {
        return notificacionRepositorio.save(notificacion);
    }

    public void delete(java.util.UUID uuid) {
        notificacionRepositorio.deleteById(uuid);
    }

    public Notificacion update(java.util.UUID uuid, Notificacion notificacion) {
        notificacion.setIdNotificacion(uuid);
        return notificacionRepositorio.save(notificacion);
    }
}
