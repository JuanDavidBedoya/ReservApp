package com.reservapp.juanb.juanm.controllers;

import java.util.List;
import java.util.Optional;
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
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{uuid}")
    public Optional<Comentario> getById(@PathVariable("uuid") UUID uuid){
        return comentarioServicio.findById(uuid);
    }
    
    @PostMapping
    public ResponseEntity<Comentario> save(@RequestBody Comentario comentario) {
        Comentario nuevoComentario = comentarioServicio.save(comentario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoComentario);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Comentario> update(@PathVariable("uuid") UUID uuid, @RequestBody Comentario comentario) {
        Comentario actualizarComentario = comentarioServicio.update(uuid, comentario);
        return ResponseEntity.ok(actualizarComentario);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        comentarioServicio.delete(uuid);
        return ResponseEntity.noContent().build();
    }   
}