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

import com.reservapp.juanb.juanm.entities.Rol;
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
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{uuid}")
    public Optional<Rol> getById(@PathVariable("uuid") UUID uuid){
        return rolServicio.findById(uuid);
    }
    
    @PostMapping
    public ResponseEntity<Rol> save(@RequestBody Rol rol) {
        Rol nuevoRol = rolServicio.save(rol);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRol);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Rol> update(@PathVariable("uuid") UUID uuid, @RequestBody Rol rol) {
        Rol actualizarRol = rolServicio.update(uuid, rol);
        return ResponseEntity.ok(actualizarRol);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        rolServicio.delete(uuid);
        return ResponseEntity.noContent().build();
    }   
}