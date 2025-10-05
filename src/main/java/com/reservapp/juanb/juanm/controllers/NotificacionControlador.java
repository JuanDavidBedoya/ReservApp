package com.reservapp.juanb.juanm.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reservapp.juanb.juanm.entities.Notificacion;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.services.NotificacionServicio;

import java.util.List;
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
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(list); // 200 OK
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Notificacion> getById(@PathVariable("uuid") UUID uuid){
        Notificacion notificacion = notificacionServicio.findById(uuid)
            .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con ID: " + uuid));
        return ResponseEntity.ok(notificacion); // 200 OK
    }

    @PostMapping
    public ResponseEntity<Notificacion> save(@RequestBody Notificacion notificacion) {
        // Validaciones básicas
        if (notificacion.getMensaje() == null || notificacion.getMensaje().trim().isEmpty()) {
            throw new BadRequestException("El mensaje de la notificación no puede estar vacío");
        }
        if (notificacion.getFechaEnvio() == null) {
            throw new BadRequestException("La fecha de envío no puede estar vacía");
        }
        if (notificacion.getTipo() == null) {
            throw new BadRequestException("La notificación debe tener un tipo asignado");
        }
        if (notificacion.getEstado() == null) {
            throw new BadRequestException("La notificación debe tener un estado asignado");
        }
        if (notificacion.getReserva() == null) {
            throw new BadRequestException("La notificación debe estar asociada a una reserva");
        }
        
        Notificacion nuevaNotificacion = notificacionServicio.save(notificacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaNotificacion); // 201 Created
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Notificacion> update(@PathVariable("uuid") UUID uuid, @RequestBody Notificacion notificacion) {
        // Verificar que existe
        if (!notificacionServicio.findById(uuid).isPresent()) {
            throw new ResourceNotFoundException("Notificación no encontrada con ID: " + uuid);
        }
        
        // Validaciones básicas
        if (notificacion.getMensaje() == null || notificacion.getMensaje().trim().isEmpty()) {
            throw new BadRequestException("El mensaje de la notificación no puede estar vacío");
        }
        if (notificacion.getFechaEnvio() == null) {
            throw new BadRequestException("La fecha de envío no puede estar vacía");
        }
        if (notificacion.getTipo() == null) {
            throw new BadRequestException("La notificación debe tener un tipo asignado");
        }
        if (notificacion.getEstado() == null) {
            throw new BadRequestException("La notificación debe tener un estado asignado");
        }
        
        Notificacion actualizarNotificacion = notificacionServicio.update(uuid, notificacion);
        return ResponseEntity.ok(actualizarNotificacion); // 200 OK
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        // Verificar que existe
        if (!notificacionServicio.findById(uuid).isPresent()) {
            throw new ResourceNotFoundException("Notificación no encontrada con ID: " + uuid);
        }
        
        notificacionServicio.delete(uuid);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}