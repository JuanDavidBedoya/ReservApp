package com.reservapp.juanb.juanm.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reservapp.juanb.juanm.dto.NotificacionRequestDTO;
import com.reservapp.juanb.juanm.dto.NotificacionResponseDTO;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
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

    private final NotificacionServicio notificacionServicio;

    public NotificacionControlador(NotificacionServicio notificacionServicio) {
        this.notificacionServicio = notificacionServicio;
    }

    @GetMapping
    public ResponseEntity<List<NotificacionResponseDTO>> getAll() {
        List<NotificacionResponseDTO> list = notificacionServicio.findAll();
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<NotificacionResponseDTO> getById(@PathVariable UUID uuid) {
        return ResponseEntity.ok(notificacionServicio.findById(uuid));
    }

    @PostMapping
    public ResponseEntity<NotificacionResponseDTO> save(@RequestBody NotificacionRequestDTO dto) {
        if (dto.mensaje() == null || dto.mensaje().trim().isEmpty()) {
            throw new BadRequestException("El mensaje no puede estar vacío");
        }
        if (dto.fechaEnvio() == null) {
            throw new BadRequestException("La fecha de envío es obligatoria");
        }

        NotificacionResponseDTO response = notificacionServicio.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<NotificacionResponseDTO> update(@PathVariable UUID uuid, @RequestBody NotificacionRequestDTO dto) {
        if (dto.mensaje() == null || dto.mensaje().trim().isEmpty()) {
            throw new BadRequestException("El mensaje no puede estar vacío");
        }
        if (dto.fechaEnvio() == null) {
            throw new BadRequestException("La fecha de envío es obligatoria");
        }

        NotificacionResponseDTO response = notificacionServicio.update(uuid, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        notificacionServicio.delete(uuid);
        return ResponseEntity.noContent().build();
    }
}