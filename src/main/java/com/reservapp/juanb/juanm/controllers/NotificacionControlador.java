// Ubicación: com/reservapp/juanb/juanm/controllers/NotificacionControlador.java
package com.reservapp.juanb.juanm.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reservapp.juanb.juanm.dto.NotificacionResponseDTO;
import com.reservapp.juanb.juanm.services.NotificacionServicio;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionControlador {

    private final NotificacionServicio notificacionServicio;

    public NotificacionControlador(NotificacionServicio notificacionServicio) {
        this.notificacionServicio = notificacionServicio;
    }

    @GetMapping
    public ResponseEntity<List<NotificacionResponseDTO>> getAll() {
        List<NotificacionResponseDTO> notificaciones = notificacionServicio.findAll();
        return notificaciones.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(notificaciones);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<NotificacionResponseDTO> getById(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity.ok(notificacionServicio.findById(uuid));
    }
}