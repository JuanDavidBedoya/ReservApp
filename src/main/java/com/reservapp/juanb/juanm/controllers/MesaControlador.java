package com.reservapp.juanb.juanm.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reservapp.juanb.juanm.entities.Mesa;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceAlreadyExistsException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.services.MesaServicio;

@RestController
@RequestMapping("/mesas")
public class MesaControlador {

    private MesaServicio mesaServicio;

    public MesaControlador(MesaServicio mesaServicio) {
        this.mesaServicio = mesaServicio;
    }

    @GetMapping
    public ResponseEntity<List<Mesa>> getAll(){
        List<Mesa> list = mesaServicio.findAll();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(list); // 200 OK
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Mesa> getById(@PathVariable("uuid") UUID uuid){
        Mesa mesa = mesaServicio.findById(uuid)
            .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada con ID: " + uuid));
        return ResponseEntity.ok(mesa); // 200 OK
    }
    
    @PostMapping
    public ResponseEntity<Mesa> save(@RequestBody Mesa mesa) {
        // Validaciones básicas
        if (mesa.getNumeroMesa() <= 0) {
            throw new BadRequestException("El número de mesa debe ser mayor a 0");
        }
        if (mesa.getCapacidad() <= 0) {
            throw new BadRequestException("La capacidad debe ser mayor a 0");
        }
        if (mesa.getEstado() == null) {
            throw new BadRequestException("La mesa debe tener un estado asignado");
        }
        
        // Validar que no exista una mesa con el mismo número
        if (mesaServicio.existsByNumeroMesa(mesa.getNumeroMesa())) {
            throw new ResourceAlreadyExistsException("Ya existe una mesa con el número: " + mesa.getNumeroMesa());
        }
        
        Mesa nuevaMesa = mesaServicio.save(mesa);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMesa); // 201 Created
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Mesa> update(@PathVariable("uuid") UUID uuid, @RequestBody Mesa mesa) {
        // Verificar que existe
        if (!mesaServicio.findById(uuid).isPresent()) {
            throw new ResourceNotFoundException("Mesa no encontrada con ID: " + uuid);
        }
        
        // Validaciones básicas
        if (mesa.getNumeroMesa() <= 0) {
            throw new BadRequestException("El número de mesa debe ser mayor a 0");
        }
        if (mesa.getCapacidad() <= 0) {
            throw new BadRequestException("La capacidad debe ser mayor a 0");
        }
        if (mesa.getEstado() == null) {
            throw new BadRequestException("La mesa debe tener un estado asignado");
        }
        
        // Validar que no exista otra mesa con el mismo número (excluyendo la actual)
        if (mesaServicio.existsByNumeroMesaAndIdNot(mesa.getNumeroMesa(), uuid)) {
            throw new ResourceAlreadyExistsException("Ya existe otra mesa con el número: " + mesa.getNumeroMesa());
        }
        
        Mesa actualizarMesa = mesaServicio.update(uuid, mesa);
        return ResponseEntity.ok(actualizarMesa); // 200 OK
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        // Verificar que existe
        if (!mesaServicio.findById(uuid).isPresent()) {
            throw new ResourceNotFoundException("Mesa no encontrada con ID: " + uuid);
        }
        
        mesaServicio.delete(uuid);
        return ResponseEntity.noContent().build(); // 204 No Content
    }   
}