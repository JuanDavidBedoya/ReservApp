package com.reservapp.juanb.juanm.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reservapp.juanb.juanm.entities.Reserva;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.CapacityExceededException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.services.ReservaServicio;

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
@RequestMapping("/reservas")
public class ReservaControlador {

    private ReservaServicio reservaServicio;

    public ReservaControlador(ReservaServicio reservaServicio) {
        this.reservaServicio = reservaServicio;
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> getAll(){
        List<Reserva> list = reservaServicio.findAll();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(list); // 200
    }
    
    @GetMapping("/{uuid}")
    public ResponseEntity<Reserva> getById(@PathVariable("uuid") UUID uuid){
        Reserva reserva = reservaServicio.findById(uuid)
            .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + uuid));
        return ResponseEntity.ok(reserva); // 200
    }

    @PostMapping
    public ResponseEntity<Reserva> save(@RequestBody Reserva reserva) {
        // Validaciones básicas adicionales
        if (reserva.getFecha() == null) {
            throw new BadRequestException("La fecha de la reserva es requerida");
        }
        if (reserva.getHora() == null) {
            throw new BadRequestException("La hora de la reserva es requerida");
        }
        if (reserva.getNumeroPersonas() <= 0) {
            throw new BadRequestException("El número de personas debe ser mayor a 0");
        }
        if (reserva.getUsuario() == null) {
            throw new BadRequestException("La reserva debe tener un usuario asociado");
        }
        if (reserva.getMesa() == null) {
            throw new BadRequestException("La reserva debe tener una mesa asociada");
        }
        
        // RF20: Validar aforo no supere 100%
        if (reservaServicio.exceedsCapacity(reserva)) {
            throw new CapacityExceededException("No se puede crear la reserva: el aforo supera el 100%");
        }
        
        Reserva nuevaReserva = reservaServicio.save(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReserva); // 201
    }
    
    @PutMapping("/{uuid}")
    public ResponseEntity<Reserva> update(@PathVariable("uuid") UUID uuid, @RequestBody Reserva reserva) {
        // Verificar que existe
        if (!reservaServicio.findById(uuid).isPresent()) {
            throw new ResourceNotFoundException("Reserva no encontrada con ID: " + uuid);
        }
        
        // Validaciones básicas para actualización
        if (reserva.getFecha() == null) {
            throw new BadRequestException("La fecha de la reserva es requerida");
        }
        if (reserva.getHora() == null) {
            throw new BadRequestException("La hora de la reserva es requerida");
        }
        if (reserva.getNumeroPersonas() <= 0) {
            throw new BadRequestException("El número de personas debe ser mayor a 0");
        }
        
        Reserva actualizarReserva = reservaServicio.update(uuid, reserva);
        return ResponseEntity.ok(actualizarReserva); // 200
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        // Verificar que existe
        if (!reservaServicio.findById(uuid).isPresent()) {
            throw new ResourceNotFoundException("Reserva no encontrada con ID: " + uuid);
        }
        
        reservaServicio.delete(uuid);
        return ResponseEntity.noContent().build(); // 204
    }
}