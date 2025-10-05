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

import com.reservapp.juanb.juanm.entities.Tipo;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceAlreadyExistsException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.services.TipoServicio;

@RestController
@RequestMapping("/tipos")
public class TipoControlador {

    private TipoServicio tipoServicio;

    public TipoControlador(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    @GetMapping
    public ResponseEntity<List<Tipo>> getAll(){
        List<Tipo> list = tipoServicio.findAll();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(list); // 200 OK
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Tipo> getById(@PathVariable("uuid") UUID uuid){
        Tipo tipo = tipoServicio.findById(uuid)
            .orElseThrow(() -> new ResourceNotFoundException("Tipo no encontrado con ID: " + uuid));
        return ResponseEntity.ok(tipo); // 200 OK
    }
    
    @PostMapping
    public ResponseEntity<Tipo> save(@RequestBody Tipo tipo) {
        // Validar que el nombre no esté vacío
        if (tipo.getNombre() == null || tipo.getNombre().trim().isEmpty()) {
            throw new BadRequestException("El nombre del tipo no puede estar vacío");
        }
        
        // Validar que no exista un tipo con el mismo nombre
        if (tipoServicio.existsByNombre(tipo.getNombre())) {
            throw new ResourceAlreadyExistsException("Ya existe un tipo con el nombre: " + tipo.getNombre());
        }
        
        Tipo nuevoTipo = tipoServicio.save(tipo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTipo); // 201 Created
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Tipo> update(@PathVariable("uuid") UUID uuid, @RequestBody Tipo tipo) {
        // Verificar que existe
        if (!tipoServicio.findById(uuid).isPresent()) {
            throw new ResourceNotFoundException("Tipo no encontrado con ID: " + uuid);
        }
        
        // Validar que el nombre no esté vacío
        if (tipo.getNombre() == null || tipo.getNombre().trim().isEmpty()) {
            throw new BadRequestException("El nombre del tipo no puede estar vacío");
        }
        
        // Validar que no exista otro tipo con el mismo nombre (excluyendo el actual)
        if (tipoServicio.existsByNombreAndIdNot(tipo.getNombre(), uuid)) {
            throw new ResourceAlreadyExistsException("Ya existe otro tipo con el nombre: " + tipo.getNombre());
        }
        
        Tipo actualizarTipo = tipoServicio.update(uuid, tipo);
        return ResponseEntity.ok(actualizarTipo); // 200 OK
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        // Verificar que existe
        if (!tipoServicio.findById(uuid).isPresent()) {
            throw new ResourceNotFoundException("Tipo no encontrado con ID: " + uuid);
        }
        
        tipoServicio.delete(uuid);
        return ResponseEntity.noContent().build(); // 204 No Content
    }   
}