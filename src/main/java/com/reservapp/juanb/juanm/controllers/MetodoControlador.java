package com.reservapp.juanb.juanm.controllers;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.reservapp.juanb.juanm.dto.MetodoDTO;
import com.reservapp.juanb.juanm.services.MetodoServicio;

@RestController
@RequestMapping("/metodos")
public class MetodoControlador {

    private final MetodoServicio metodoServicio;

    public MetodoControlador(MetodoServicio metodoServicio) {
        this.metodoServicio = metodoServicio;
    }

    @GetMapping
    public ResponseEntity<List<MetodoDTO>> getAll() {
        List<MetodoDTO> list = metodoServicio.findAll();
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<MetodoDTO> getById(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity.ok(metodoServicio.findById(uuid));
    }
}