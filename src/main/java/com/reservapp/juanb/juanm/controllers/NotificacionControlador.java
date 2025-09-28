package com.reservapp.juanb.juanm.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reservapp.juanb.juanm.entities.Notificacion;
import com.reservapp.juanb.juanm.services.NotificacionServicio;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionControlador {

    private NotificacionServicio notificacionServicio;

    public NotificacionControlador(NotificacionServicio notificacionServicio) {
        this.notificacionServicio = notificacionServicio;
    }

    @GetMapping
    public ResponseEntity<List<Notificacion>> getAll(){
        List<Notificacion> list = notificacionServicio.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{uuid}")
    public Optional<Notificacion> getById(@PathVariable("uuid") UUID uuid){
        return notificacionServicio.findById(uuid);
    }

    @PostMapping
    public ResponseEntity<Notificacion> save(@RequestBody Notificacion notificacion) {
        Notificacion nuevaNotificacion = notificacionServicio.save(notificacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaNotificacion);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Notificacion> update(@PathVariable("uuid") UUID uuid, @RequestBody Notificacion notificacion) {
        Notificacion actualizarNotificacion = notificacionServicio.update(uuid, notificacion);
        return ResponseEntity.ok(actualizarNotificacion);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        notificacionServicio.delete(uuid);
        return ResponseEntity.noContent().build();
    }
}
