package com.reservapp.juanb.juanm.controllers;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.reservapp.juanb.juanm.dto.TipoDTO;
import com.reservapp.juanb.juanm.services.TipoServicio;

@RestController
@RequestMapping("/tipos")
public class TipoControlador {

    private final TipoServicio tipoServicio;

    public TipoControlador(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    //Solo tiene método para Get lista y Get por Id debido a la lógica de Negocio
    @GetMapping
    public ResponseEntity<List<TipoDTO>> getAll() {
        List<TipoDTO> list = tipoServicio.findAll();
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<TipoDTO> getById(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity.ok(tipoServicio.findById(uuid));
    }
}