package com.reservapp.juanb.juanm.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reservapp.juanb.juanm.entities.Estado;
import com.reservapp.juanb.juanm.services.EstadoServicio;

import java.util.List;
import java.util.Optional;
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
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{uuid}")
    public Optional<Estado> getById(@PathVariable("uuid") UUID uuid){
        return estadoServicio.findById(uuid);
    }

    @PostMapping
    public ResponseEntity<Estado> save(@RequestBody Estado estado) {
        Estado nuevoEstado = estadoServicio.save(estado);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEstado);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Estado> update(@PathVariable("uuid") UUID uuid, @RequestBody Estado estado) {
        Estado actualizarEstado = estadoServicio.update(uuid, estado);
        return ResponseEntity.ok(actualizarEstado);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        estadoServicio.delete(uuid);
        return ResponseEntity.noContent().build();
    }
}
