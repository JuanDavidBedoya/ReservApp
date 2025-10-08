package com.reservapp.juanb.juanm.controllers;

import org.springframework.web.bind.annotation.*;
import com.reservapp.juanb.juanm.dto.EstadoDTO;
import com.reservapp.juanb.juanm.services.EstadoServicio;
import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/estados")
public class EstadoControlador {

    private final EstadoServicio estadoServicio;

    public EstadoControlador(EstadoServicio estadoServicio) {
        this.estadoServicio = estadoServicio;
    }

    //Solo tiene método para Get lista debido a la lógica de Negocio
    @GetMapping
    public ResponseEntity<List<EstadoDTO>> getAll() {
        List<EstadoDTO> list = estadoServicio.findAll();
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

}