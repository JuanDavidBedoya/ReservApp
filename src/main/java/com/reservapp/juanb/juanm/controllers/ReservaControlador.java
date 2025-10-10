package com.reservapp.juanb.juanm.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reservapp.juanb.juanm.dto.ReservaRequestDTO;
import com.reservapp.juanb.juanm.dto.ReservaResponseDTO;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.services.ReservaServicio;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/reservas")
public class ReservaControlador {

    private final ReservaServicio reservaServicio;

    public ReservaControlador(ReservaServicio reservaServicio) {
        this.reservaServicio = reservaServicio;
    }
    
    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> getAll() {
        List<ReservaResponseDTO> list = reservaServicio.findAll();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(list); // 200
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ReservaResponseDTO> getById(@PathVariable("uuid") UUID uuid) {
        ReservaResponseDTO reserva = reservaServicio.findById(uuid);
        return ResponseEntity.ok(reserva); // 200
    }

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> save(@RequestBody ReservaRequestDTO dto) {

        // Validaciones básicas adicionales
        if (dto.fecha() == null) {
            throw new BadRequestException("La fecha de la reserva es requerida");
        }
        if (dto.hora() == null) {
            throw new BadRequestException("La hora de la reserva es requerida");
        }
        if (dto.numeroPersonas() <= 0) {
            throw new BadRequestException("El número de personas debe ser mayor a 0");
        }
        if (dto.cedulaUsuario() == null) {
            throw new BadRequestException("La reserva debe tener un usuario asociado");
        }
        
        ReservaResponseDTO nuevaReserva = reservaServicio.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReserva); // 201
    }
    @PutMapping("/{uuid}")
    public ResponseEntity<ReservaResponseDTO> update(@PathVariable("uuid") UUID uuid,
                                                     @RequestBody ReservaRequestDTO dto) {
        if (dto.fecha() == null) {
            throw new BadRequestException("La fecha de la reserva es requerida");
        }
        if (dto.hora() == null) {
            throw new BadRequestException("La hora de la reserva es requerida");
        }
        if (dto.numeroPersonas() <= 0) {
            throw new BadRequestException("El número de personas debe ser mayor a 0");
        }

        ReservaResponseDTO actualizada = reservaServicio.update(uuid, dto);
        return ResponseEntity.ok(actualizada); // 200
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        reservaServicio.delete(uuid);
        return ResponseEntity.noContent().build(); // 204
    }
    
    //Cancelar una reserva
    @PatchMapping("/{uuid}/cancelar")
    public ResponseEntity<Void> cancelarReserva(@PathVariable UUID uuid) {
        reservaServicio.cancel(uuid);
        return ResponseEntity.noContent().build(); // 204
    }

    @GetMapping("/usuario/{cedula}")
    public ResponseEntity<List<ReservaResponseDTO>> getByUsuario(@PathVariable("cedula") String cedula) {
        List<ReservaResponseDTO> reservas = reservaServicio.findByUsuario(cedula);
        if (reservas.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(reservas); // 200
    }
}
