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

import com.reservapp.juanb.juanm.entities.Tipo;
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
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{uuid}")
    public Optional<Tipo> getById(@PathVariable("uuid") UUID uuid){
        return tipoServicio.findById(uuid);
    }
    
    @PostMapping
    public ResponseEntity<Tipo> save(@RequestBody Tipo tipo) {
        Tipo nuevTipo = tipoServicio.save(tipo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevTipo);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Tipo> update(@PathVariable("uuid") UUID uuid, @RequestBody Tipo tipo) {
        Tipo actualizarTipo = tipoServicio.update(uuid, tipo);
        return ResponseEntity.ok(actualizarTipo);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        tipoServicio.delete(uuid);
        return ResponseEntity.noContent().build();
    }   
}