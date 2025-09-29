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

import com.reservapp.juanb.juanm.entities.Mesa;
import com.reservapp.juanb.juanm.services.MesaServicio;

@RestController
@RequestMapping("/mesas")
public class MesaControlador {

    private MesaServicio mesaServicio;

    public MesaControlador(MesaServicio mesaServicio) {
        this.mesaServicio = mesaServicio;
    }

    @GetMapping
    public ResponseEntity<List<Mesa>> getAll(){
        List<Mesa> list = mesaServicio.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{uuid}")
    public Optional<Mesa> getById(@PathVariable("uuid") UUID uuid){
        return mesaServicio.findById(uuid);
    }
    
    @PostMapping
    public ResponseEntity<Mesa> save(@RequestBody Mesa mesa) {
        Mesa nuevaMesa = mesaServicio.save(mesa);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMesa);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Mesa> update(@PathVariable("uuid") UUID uuid, @RequestBody Mesa mesa) {
        Mesa actualizarMesa = mesaServicio.update(uuid, mesa);
        return ResponseEntity.ok(actualizarMesa);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        mesaServicio.delete(uuid);
        return ResponseEntity.noContent().build();
    }   
}