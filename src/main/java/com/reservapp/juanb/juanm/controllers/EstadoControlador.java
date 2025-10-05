package com.reservapp.juanb.juanm.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reservapp.juanb.juanm.entities.Estado;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceAlreadyExistsException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.services.EstadoServicio;

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
@RequestMapping("/estados")
public class EstadoControlador {

    private EstadoServicio estadoServicio;

    public EstadoControlador(EstadoServicio estadoServicio) {
        this.estadoServicio = estadoServicio;
    }

    @GetMapping
    public ResponseEntity<List<Estado>> getAll(){
        List<Estado> list = estadoServicio.findAll();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(list); // 200 OK
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Estado> getById(@PathVariable("uuid") UUID uuid){
        Estado estado = estadoServicio.findById(uuid)
            .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado con ID: " + uuid));
        return ResponseEntity.ok(estado); // 200 OK
    }

    @PostMapping
    public ResponseEntity<Estado> save(@RequestBody Estado estado) {
        // Validar que el nombre no esté vacío
        if (estado.getNombre() == null || estado.getNombre().trim().isEmpty()) {
            throw new BadRequestException("El nombre del estado no puede estar vacío");
        }
        
        // Validar que no exista un estado con el mismo nombre
        if (estadoServicio.existsByNombre(estado.getNombre())) {
            throw new ResourceAlreadyExistsException("Ya existe un estado con el nombre: " + estado.getNombre());
        }
        
        Estado nuevoEstado = estadoServicio.save(estado);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEstado); // 201 Created
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Estado> update(@PathVariable("uuid") UUID uuid, @RequestBody Estado estado) {
        // Verificar que existe
        if (!estadoServicio.findById(uuid).isPresent()) {
            throw new ResourceNotFoundException("Estado no encontrado con ID: " + uuid);
        }
        
        // Validar que el nombre no esté vacío
        if (estado.getNombre() == null || estado.getNombre().trim().isEmpty()) {
            throw new BadRequestException("El nombre del estado no puede estar vacío");
        }
        
        // Validar que no exista otro estado con el mismo nombre (excluyendo el actual)
        if (estadoServicio.existsByNombreAndIdNot(estado.getNombre(), uuid)) {
            throw new ResourceAlreadyExistsException("Ya existe otro estado con el nombre: " + estado.getNombre());
        }
        
        Estado actualizarEstado = estadoServicio.update(uuid, estado);
        return ResponseEntity.ok(actualizarEstado); // 200 OK
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        // Verificar que existe
        if (!estadoServicio.findById(uuid).isPresent()) {
            throw new ResourceNotFoundException("Estado no encontrado con ID: " + uuid);
        }
        
        estadoServicio.delete(uuid);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}