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

import com.reservapp.juanb.juanm.entities.Rol;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceAlreadyExistsException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.services.RolServicio;

@RestController
@RequestMapping("/roles")
public class RolControlador {

    private RolServicio rolServicio;

    public RolControlador(RolServicio rolServicio) {
        this.rolServicio = rolServicio;
    }

    @GetMapping
    public ResponseEntity<List<Rol>> getAll(){
        List<Rol> list = rolServicio.findAll();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(list); // 200 OK
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Rol> getById(@PathVariable("uuid") UUID uuid){
        Rol rol = rolServicio.findById(uuid)
            .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con ID: " + uuid));
        return ResponseEntity.ok(rol); // 200 OK
    }
    
    @PostMapping
    public ResponseEntity<Rol> save(@RequestBody Rol rol) {
        // Validar que el nombre no esté vacío
        if (rol.getNombre() == null || rol.getNombre().trim().isEmpty()) {
            throw new BadRequestException("El nombre del rol no puede estar vacío");
        }
        
        // Validar que no exista un rol con el mismo nombre
        if (rolServicio.existsByNombre(rol.getNombre())) {
            throw new ResourceAlreadyExistsException("Ya existe un rol con el nombre: " + rol.getNombre());
        }
        
        Rol nuevoRol = rolServicio.save(rol);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRol); // 201 Created
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Rol> update(@PathVariable("uuid") UUID uuid, @RequestBody Rol rol) {
        // Verificar que existe
        if (!rolServicio.findById(uuid).isPresent()) {
            throw new ResourceNotFoundException("Rol no encontrado con ID: " + uuid);
        }
        
        // Validar que el nombre no esté vacío
        if (rol.getNombre() == null || rol.getNombre().trim().isEmpty()) {
            throw new BadRequestException("El nombre del rol no puede estar vacío");
        }
        
        // Validar que no exista otro rol con el mismo nombre (excluyendo el actual)
        if (rolServicio.existsByNombreAndIdNot(rol.getNombre(), uuid)) {
            throw new ResourceAlreadyExistsException("Ya existe otro rol con el nombre: " + rol.getNombre());
        }
        
        Rol actualizarRol = rolServicio.update(uuid, rol);
        return ResponseEntity.ok(actualizarRol); // 200 OK
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        // Verificar que existe
        if (!rolServicio.findById(uuid).isPresent()) {
            throw new ResourceNotFoundException("Rol no encontrado con ID: " + uuid);
        }
        
        rolServicio.delete(uuid);
        return ResponseEntity.noContent().build(); // 204 No Content
    }   
}