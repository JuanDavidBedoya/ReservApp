package com.reservapp.juanb.juanm.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reservapp.juanb.juanm.entities.Metodo;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceAlreadyExistsException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.services.MetodoServicio;

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
@RequestMapping("/metodos")
public class MetodoControlador {

    private MetodoServicio metodoServicio;

    public MetodoControlador(MetodoServicio metodoServicio) {
        this.metodoServicio = metodoServicio;
    }

    @GetMapping
    public ResponseEntity<List<Metodo>> getAll(){
        List<Metodo> list = metodoServicio.findAll();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(list); // 200 OK
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Metodo> getById(@PathVariable("uuid") UUID uuid){
        Metodo metodo = metodoServicio.findById(uuid)
            .orElseThrow(() -> new ResourceNotFoundException("Método no encontrado con ID: " + uuid));
        return ResponseEntity.ok(metodo); // 200 OK
    }

    @PostMapping
    public ResponseEntity<Metodo> save(@RequestBody Metodo metodo) {
        // Validar que el nombre no esté vacío
        if (metodo.getNombre() == null || metodo.getNombre().trim().isEmpty()) {
            throw new BadRequestException("El nombre del método no puede estar vacío");
        }
        
        // Validar que no exista un método con el mismo nombre
        if (metodoServicio.existsByNombre(metodo.getNombre())) {
            throw new ResourceAlreadyExistsException("Ya existe un método con el nombre: " + metodo.getNombre());
        }
        
        Metodo nuevoMetodo = metodoServicio.save(metodo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMetodo); // 201 Created
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Metodo> update(@PathVariable("uuid") UUID uuid, @RequestBody Metodo metodo) {
        // Verificar que existe
        if (!metodoServicio.findById(uuid).isPresent()) {
            throw new ResourceNotFoundException("Método no encontrado con ID: " + uuid);
        }
        
        // Validar que el nombre no esté vacío
        if (metodo.getNombre() == null || metodo.getNombre().trim().isEmpty()) {
            throw new BadRequestException("El nombre del método no puede estar vacío");
        }
        
        // Validar que no exista otro método con el mismo nombre (excluyendo el actual)
        if (metodoServicio.existsByNombreAndIdNot(metodo.getNombre(), uuid)) {
            throw new ResourceAlreadyExistsException("Ya existe otro método con el nombre: " + metodo.getNombre());
        }
        
        Metodo actualizarMetodo = metodoServicio.update(uuid, metodo);
        return ResponseEntity.ok(actualizarMetodo); // 200 OK
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        // Verificar que existe
        if (!metodoServicio.findById(uuid).isPresent()) {
            throw new ResourceNotFoundException("Método no encontrado con ID: " + uuid);
        }
        
        metodoServicio.delete(uuid);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}