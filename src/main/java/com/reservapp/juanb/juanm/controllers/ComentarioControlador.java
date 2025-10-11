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

import com.reservapp.juanb.juanm.dto.ComentarioRequestDTO;
import com.reservapp.juanb.juanm.dto.ComentarioResponseDTO;
import com.reservapp.juanb.juanm.dto.PromedioSemanalDTO;
import com.reservapp.juanb.juanm.services.ComentarioServicio;

@RestController
@RequestMapping("/comentarios")
public class ComentarioControlador {

    //Inyección de Dependencias
    private final ComentarioServicio comentarioServicio;

    public ComentarioControlador(ComentarioServicio comentarioServicio) {
        this.comentarioServicio = comentarioServicio;
    }

    @GetMapping
    public ResponseEntity<List<ComentarioResponseDTO>> getAll() {
        List<ComentarioResponseDTO> list = comentarioServicio.findAll();
        if (list.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ComentarioResponseDTO> getById(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity.ok(comentarioServicio.findById(uuid));
    }

    @PostMapping
    public ResponseEntity<ComentarioResponseDTO> save(@RequestBody ComentarioRequestDTO dto) {
        ComentarioResponseDTO nuevo = comentarioServicio.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ComentarioResponseDTO> update(@PathVariable("uuid") UUID uuid,
                                                        @RequestBody ComentarioRequestDTO dto) {
        ComentarioResponseDTO actualizado = comentarioServicio.update(uuid, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        comentarioServicio.delete(uuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/promedio-semanal")
    public ResponseEntity<PromedioSemanalDTO> getPromedioSemanal() {
        PromedioSemanalDTO promedio = comentarioServicio.getPromedioSemanal();
        return ResponseEntity.ok(promedio);
    }
}