package com.reservapp.juanb.juanm.controllers;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.reservapp.juanb.juanm.dto.RolDTO;
import com.reservapp.juanb.juanm.services.RolServicio;

@RestController
@RequestMapping("/roles")
public class RolControlador {

    private final RolServicio rolServicio;

    public RolControlador(RolServicio rolServicio) {
        this.rolServicio = rolServicio;
    }

    //Solo tiene método para Get lista y Get por Id debido a la lógica de Negocio
    @GetMapping
    public ResponseEntity<List<RolDTO>> getAll() {
        List<RolDTO> list = rolServicio.findAll();
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<RolDTO> getById(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity.ok(rolServicio.findById(uuid));
    }
}