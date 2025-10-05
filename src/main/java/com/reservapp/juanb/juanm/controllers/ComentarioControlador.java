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

import com.reservapp.juanb.juanm.entities.Comentario;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.services.ComentarioServicio;

@RestController
@RequestMapping("/comentarios")
public class ComentarioControlador {

    private ComentarioServicio comentarioServicio;

    public ComentarioControlador(ComentarioServicio comentarioServicio) {
        this.comentarioServicio = comentarioServicio;
    }

    @GetMapping
    public ResponseEntity<List<Comentario>> getAll(){
        List<Comentario> list = comentarioServicio.findAll();
        if(list.isEmpty()){
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(list); // 200 OK
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Comentario> getById(@PathVariable("uuid") UUID uuid){
        Comentario comentario = comentarioServicio.findById(uuid)
            .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado con ID: " + uuid));
        return ResponseEntity.ok(comentario); // 200 OK
    }
    
    @PostMapping
    public ResponseEntity<Comentario> save(@RequestBody Comentario comentario) {
        try {
            // Validaciones básicas
            if (comentario.getMensaje() == null || comentario.getMensaje().trim().isEmpty()) {
                throw new BadRequestException("El mensaje del comentario no puede estar vacío");
            }
            if (comentario.getPuntuacion() < 1 || comentario.getPuntuacion() > 5) {
                throw new BadRequestException("La puntuación debe estar entre 1 y 5");
            }
            
            Comentario nuevoComentario = comentarioServicio.save(comentario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoComentario); // 201 Created
        } catch (Exception e) {
            throw new BadRequestException("Error al crear el comentario: " + e.getMessage());
        }
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Comentario> update(@PathVariable("uuid") UUID uuid, @RequestBody Comentario comentario) {
        // Verificar que existe antes de actualizar
        if (!comentarioServicio.findById(uuid).isPresent()) {
            throw new ResourceNotFoundException("Comentario no encontrado con ID: " + uuid);
        }
        
        // Validaciones
        if (comentario.getMensaje() == null || comentario.getMensaje().trim().isEmpty()) {
            throw new BadRequestException("El mensaje del comentario no puede estar vacío");
        }
        
        comentario.setIdComentario(uuid);
        Comentario actualizarComentario = comentarioServicio.save(comentario);
        return ResponseEntity.ok(actualizarComentario); // 200 OK
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        // Verificar que existe antes de eliminar
        if (!comentarioServicio.findById(uuid).isPresent()) {
            throw new ResourceNotFoundException("Comentario no encontrado con ID: " + uuid);
        }
        
        comentarioServicio.delete(uuid);
        return ResponseEntity.noContent().build(); // 204 No Content
    }   
}