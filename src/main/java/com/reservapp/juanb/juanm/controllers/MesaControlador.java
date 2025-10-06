package com.reservapp.juanb.juanm.controllers;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.reservapp.juanb.juanm.dto.MesaDTO;
import com.reservapp.juanb.juanm.services.MesaServicio;

@RestController
@RequestMapping("/mesas")
public class MesaControlador {

    private final MesaServicio mesaServicio;

    public MesaControlador(MesaServicio mesaServicio) {
        this.mesaServicio = mesaServicio;
    }

    @GetMapping
    public ResponseEntity<List<MesaDTO>> getAll() {
        List<MesaDTO> list = mesaServicio.findAll();
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<MesaDTO> getById(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity.ok(mesaServicio.findById(uuid));
    }
}