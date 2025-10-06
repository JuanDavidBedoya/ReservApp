package com.reservapp.juanb.juanm.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reservapp.juanb.juanm.dto.PagoRequestDTO;
import com.reservapp.juanb.juanm.dto.PagoResponseDTO;
import com.reservapp.juanb.juanm.services.PagoServicio;
import jakarta.validation.Valid;

import java.util.List;
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
@RequestMapping("/pagos")
public class PagoControlador {

    private final PagoServicio pagoServicio;
    
    public PagoControlador(PagoServicio pagoServicio) {
        this.pagoServicio = pagoServicio;
    }

    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> getAll() {
        List<PagoResponseDTO> pagos = pagoServicio.findAll();
        if (pagos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<PagoResponseDTO> getById(@PathVariable UUID uuid) {
        return ResponseEntity.ok(pagoServicio.findById(uuid));
    }

    @PostMapping
    public ResponseEntity<PagoResponseDTO> save(@Valid @RequestBody PagoRequestDTO dto) {
        PagoResponseDTO nuevo = pagoServicio.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<PagoResponseDTO> update(@PathVariable UUID uuid, @Valid @RequestBody PagoRequestDTO dto) {
        return ResponseEntity.ok(pagoServicio.update(uuid, dto));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        pagoServicio.delete(uuid);
        return ResponseEntity.noContent().build();
    }
}