package com.reservapp.juanb.juanm.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reservapp.juanb.juanm.entities.Metodo;
import com.reservapp.juanb.juanm.services.MetodoServicio;

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
@RequestMapping("/metodos")
public class MetodoControlador {

    private MetodoServicio metodoServicio;

    public MetodoControlador(MetodoServicio metodoServicio) {
        this.metodoServicio = metodoServicio;
    }

    @GetMapping
    public ResponseEntity<List<Metodo>> getAll(){
        List<Metodo> list = metodoServicio.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{uuid}")
    public Optional<Metodo> getById(@PathVariable("uuid") UUID uuid){
        return metodoServicio.findById(uuid);
    }

    @PostMapping
    public ResponseEntity<Metodo> save(@RequestBody Metodo metodo) {
        Metodo nuevoMetodo = metodoServicio.save(metodo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMetodo);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Metodo> update(@PathVariable("uuid") UUID uuid, @RequestBody Metodo metodo) {
        Metodo actualizarMetodo = metodoServicio.update(uuid, metodo);
        return ResponseEntity.ok(actualizarMetodo);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        metodoServicio.delete(uuid);
        return ResponseEntity.noContent().build();
    }
}
